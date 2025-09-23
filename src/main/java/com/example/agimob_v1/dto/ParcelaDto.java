package com.example.agimob_v1.dto;

public class ParcelaDto {
    private int numeroParcela;
    private double valorTotalParcela;
    private double valorJurosParcela;
    private double amortizacao;
    private double saldoDevedor;

    public ParcelaDto() {
    }

    public ParcelaDto(int numeroParcela, double valorTotalParcela, double valorJurosParcela, double amortizacao, double saldoDevedor) {
        this.numeroParcela = numeroParcela;
        this.valorTotalParcela = valorTotalParcela;
        this.valorJurosParcela = valorJurosParcela;
        this.amortizacao = amortizacao;
        this.saldoDevedor = saldoDevedor;
    }

    public int getNumeroParcela() {
        return numeroParcela;
    }

    public void setNumeroParcela(int numeroParcela) {
        this.numeroParcela = numeroParcela;
    }

    public double getValorTotalParcela() {
        return valorTotalParcela;
    }

    public void setValorTotalParcela(double valorTotalParcela) {
        this.valorTotalParcela = valorTotalParcela;
    }

    public double getValorJurosParcela() {
        return valorJurosParcela;
    }

    public void setValorJurosParcela(double valorJurosParcela) {
        this.valorJurosParcela = valorJurosParcela;
    }

    public double getAmortizacao() {
        return amortizacao;
    }

    public void setAmortizacao(double amortizacao) {
        this.amortizacao = amortizacao;
    }

    public double getSaldoDevedor() {
        return saldoDevedor;
    }

    public void setSaldoDevedor(double saldoDevedor) {
        this.saldoDevedor = saldoDevedor;
    }

}
