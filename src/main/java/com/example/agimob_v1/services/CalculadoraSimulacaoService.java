    package com.example.agimob_v1.services;

    import com.example.agimob_v1.dto.*;

    import com.example.agimob_v1.model.Simulacao;
    import com.example.agimob_v1.model.Taxa;
    import com.example.agimob_v1.services.mappers.SimulacaoResponseMapper;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;

    import java.util.ArrayList;
    import java.util.List;


    @Service
    @RequiredArgsConstructor
    public class CalculadoraSimulacaoService{

        private final SimulacaoResponseMapper simulacaoResponseDtoMapper;
        private final TaxaService taxaService;

        public SimulacaoResponseDto calcularParcelas(Simulacao simulacao){


            Taxa taxa;

            if(simulacao.getTipoUsuario().equalsIgnoreCase("CLIENTE")){
                taxa = taxaService.determinarTaxa(simulacao);
            }else {
                taxa = taxaService.getTaxaPadraoVigente();
            }

          if(simulacao.getTipo_modalidade().equalsIgnoreCase("SAC")){


              List<ParcelaDto> parcelas = sac(simulacao, taxa);
              InformacoesAdicionaisDto informacoesAdicionais = calcularInformacoesAdicionais(simulacao, parcelas, taxa);

              SacResponseDto sacResponse = SacResponseDto.builder()
                      .parcelas(parcelas)
                      .informacoesAdicionais(informacoesAdicionais)
                      .build();


              return simulacaoResponseDtoMapper.toSacReponseDto(simulacao.getId(), simulacao.getTipo_modalidade(), taxa, simulacao.getTipoUsuario(), sacResponse);
           }
            else if(simulacao.getTipo_modalidade().equalsIgnoreCase("PRICE")){


              List<ParcelaDto> parcelas = price(simulacao, taxa);
              InformacoesAdicionaisDto informacoesAdicionais = calcularInformacoesAdicionais(simulacao, parcelas, taxa);

              PriceResponseDto priceResponse = PriceResponseDto.builder()
                      .parcelas(parcelas)
                      .informacoesAdicionais(informacoesAdicionais)
                      .build();


              return simulacaoResponseDtoMapper.toPriceReponseDto(simulacao.getId(), simulacao.getTipo_modalidade(),taxa, simulacao.getTipoUsuario(), priceResponse);
            }
            else{

              List<ParcelaDto> parcelasSac = sac(simulacao, taxa);
              List<ParcelaDto> parcelasPrice = price(simulacao, taxa);

              InformacoesAdicionaisDto informacoesAdicionaisSac = calcularInformacoesAdicionais(simulacao, parcelasSac, taxa);
              InformacoesAdicionaisDto informacoesAdicionaisPrice = calcularInformacoesAdicionais(simulacao, parcelasPrice, taxa);

              SacResponseDto sacResponse = SacResponseDto.builder()
                      .parcelas(parcelasSac)
                      .informacoesAdicionais(informacoesAdicionaisSac)
                      .build();


              PriceResponseDto priceResponse = PriceResponseDto.builder()
                      .parcelas(parcelasPrice)
                      .informacoesAdicionais(informacoesAdicionaisPrice)
                      .build();



              return simulacaoResponseDtoMapper.toAmbosResponseDto(simulacao.getId(), simulacao.getTipo_modalidade(), taxa, simulacao.getTipoUsuario(),  sacResponse, priceResponse);


            }

        }



        private List<ParcelaDto> sac(Simulacao simulacao, Taxa taxa){

            int prazo = simulacao.getPrazo();
            double saldoDevedor = simulacao.getValor_total()-simulacao.getValor_entrada();
            double amortizacao = saldoDevedor/prazo;
            double taxaMensal = taxa.getValor_taxa()/12;

            List<ParcelaDto> parcelas = new ArrayList<>();
            for (int i = 1; i <= prazo ; i++) {
                double valorJurosParcela = saldoDevedor * taxaMensal;
                double valorTotalParcela = amortizacao + valorJurosParcela;

                saldoDevedor -= amortizacao;

                parcelas.add(
                        new ParcelaDto(
                                i,
                                valorTotalParcela,
                                valorJurosParcela,
                                amortizacao,
                                saldoDevedor
                        ));

            }
             return parcelas;
        }

        private List<ParcelaDto> price(Simulacao simulacao, Taxa taxa){
            //m = p * (j*[(1+j)^n]/[(1+j)^n]-1)
            //m = parcela fixa
            //p = valor do financiamento
            //j = taxaAplicada de jurosMesOutrosBancos
            //n = numero de parcelas

            int prazo = simulacao.getPrazo();
            double taxaMensal = taxa.getValor_taxa()/12;
            double saldoDevedor = simulacao.getValor_total()-simulacao.getValor_entrada();
            double parcelaFixa = saldoDevedor*
                    (taxaMensal*(Math.pow(1+taxaMensal,prazo))
                            /(Math.pow(1+ taxaMensal,prazo)-1)
                    );

            List<ParcelaDto> parcelas = new ArrayList<>();

            for (int i = 1; i <= prazo ; i++) {
                double jurosSaldoDevedor = saldoDevedor* taxaMensal;
                double amortizacaoMes = parcelaFixa - jurosSaldoDevedor;

                saldoDevedor -= amortizacaoMes;

                parcelas.add(
                        new ParcelaDto(
                                i,
                                parcelaFixa,
                                jurosSaldoDevedor,
                                amortizacaoMes,
                                saldoDevedor
                        )
                );
            }

            return parcelas;

        }



        public InformacoesAdicionaisDto calcularInformacoesAdicionais(Simulacao simulacao, List<ParcelaDto> parcelas, Taxa taxa){

            double primeiraParcela = valorPrimeiraParcela(parcelas);
            double ultimaParcela = valorUltimaParcela(parcelas);
            double valorTotalFinanciamento = parcelas.stream().mapToDouble(ParcelaDto::getValorTotalParcela).sum();
            double valorTotalJuros = valorTotalFinanciamento(parcelas);
            double rendaComprometida = rendaComprometida(simulacao, parcelas);
            double diferencaPriceSac = diferencaPriceSac(simulacao, taxa);


            return new InformacoesAdicionaisDto(taxa.getValor_taxa(), primeiraParcela, ultimaParcela, valorTotalFinanciamento, valorTotalJuros, rendaComprometida, diferencaPriceSac);

        }

        public double valorPrimeiraParcela(List<ParcelaDto> parcelas){
            return parcelas.getFirst().getValorTotalParcela();
        }

        public double valorUltimaParcela(List<ParcelaDto> parcelas){
            return parcelas.getLast().getValorTotalParcela();
        }

        public double valorTotalFinanciamento(List<ParcelaDto> parcelas){
            return parcelas.stream().mapToDouble(ParcelaDto::getValorJurosParcela).sum();
        }

        public double rendaComprometida(Simulacao simulacao, List<ParcelaDto> parcelas){
            return (valorPrimeiraParcela(parcelas)/rendaTotal(simulacao))*100;
        }

        public double rendaTotal(Simulacao simulacao){
            return simulacao.getRenda_usuario()+ simulacao.getRenda_participante();
        }

        public double diferencaPriceSac(Simulacao simulacao, Taxa taxa){
            double valorTotalSac = valorTotalFinanciamento(sac(simulacao, taxa));
            double valorTotalPrice = valorTotalFinanciamento(price(simulacao, taxa));

            return valorTotalPrice - valorTotalSac;
        }
        

    }
