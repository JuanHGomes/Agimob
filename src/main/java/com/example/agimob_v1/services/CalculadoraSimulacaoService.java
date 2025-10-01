    package com.example.agimob_v1.services;

    import com.example.agimob_v1.dto.*;

    import com.example.agimob_v1.model.Simulacao;
    import org.springframework.stereotype.Service;

    import javax.crypto.spec.OAEPParameterSpec;
    import java.util.ArrayList;
    import java.util.List;


    @Service
    public class CalculadoraSimulacaoService {


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
            return (rendaTotal(simulacao)/valorPrimeiraParcela(parcelas))*100;
        }

        public double rendaTotal(Simulacao simulacao){
            return simulacao.getRenda_usuario()+ simulacao.getRenda_participante();
        }

        public InformacoesAdicionaisDto calcularInformacoesAdicionais(Simulacao simulacao, List<ParcelaDto> parcelas){

            double primeiraParcela = valorPrimeiraParcela(parcelas);
            double ultimaParcela = valorUltimaParcela(parcelas);
            double valorTotalFinanciamento = parcelas.stream().mapToDouble(ParcelaDto::getValorTotalParcela).sum();
            double valorTotalJuros = valorTotalFinanciamento(parcelas);
            double rendaComprometida = rendaComprometida(simulacao, parcelas);
            // FALTA DIFERENÇA PRICE - SAC

            return new InformacoesAdicionaisDto(primeiraParcela, ultimaParcela, valorTotalFinanciamento, valorTotalJuros, rendaComprometida);

        }

        public List<ParcelaDto> sac(Simulacao simulacao){
            //adiciona o metodo de conversao a uma variavel do tipo int
            int prazo = simulacao.getPrazo();
            double saldoDevedor = simulacao.getValor_total()-simulacao.getValor_entrada();
            double amortizacao = saldoDevedor/prazo;

            List<ParcelaDto> parcelas = new ArrayList<>();
            for (int i = 1; i <= prazo ; i++) {
                double valorJurosParcela = saldoDevedor * simulacao.getId_taxa().getValor_taxa();
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

        public List<ParcelaDto> price(Simulacao simulacao){
            //m = p * (j*[(1+j)^n]/[(1+j)^n]-1)
            //m = parcela fixa
            //p = valor do financiamento
            //j = taxaAplicada de jurosMesOutrosBancos
            //n = numero de parcelas

            //adiciona o metodo de conversao a uma variavel do tipo int
            int prazo = simulacao.getPrazo();
            double juros = simulacao.getId_taxa().getValor_taxa();

            List<ParcelaDto> parcelas = new ArrayList<>();
            double saldoDevedor = simulacao.getValor_total()-simulacao.getValor_entrada();

            double parcelaFixa = saldoDevedor*
                    (juros*(Math.pow(1+juros,prazo)
                            /(Math.pow(1+ juros,prazo)-1))
                    );

            for (int i = 1; i <= prazo ; i++) {
                double jurosSaldoDevedor = saldoDevedor* juros;
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


        public SimulacaoAgibankResponseDto agibank(Simulacao simulacao){
            int prazo = simulacao.getPrazo();
            double juros = simulacao.getId_taxa().getValor_taxa();

            double saldoDevedor = simulacao.getValor_total()-simulacao.getValor_entrada();
            double amortizacao = saldoDevedor/prazo;
            double valorJurosParcela = saldoDevedor * juros;
            double valorTotalParcela = amortizacao + valorJurosParcela;

            double valorTotalFinanciamento = price(simulacao).stream().mapToDouble(ParcelaDto::getValorTotalParcela).sum();
            double valorTotalJuros = price(simulacao).stream().mapToDouble(ParcelaDto::getValorJurosParcela).sum();


            return new SimulacaoAgibankResponseDto(valorJurosParcela,valorTotalFinanciamento,valorTotalJuros);
        }

    }
