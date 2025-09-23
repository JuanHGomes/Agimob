package com.example.agimob_v1.dto;

public class SimulacaoAgibankResponse {
    private final static int prazoMaximo = 420;
    private double parcela;
    private double valorTotal;
    private double totalJuros;

    public SimulacaoAgibankResponse() {
    }

    public SimulacaoAgibankResponse(double parcela, double valorTotal, double totalJuros) {
        this.parcela = parcela;
        this.valorTotal = valorTotal;
        this.totalJuros = totalJuros;
    }

    public double getParcela() {
        return parcela;
    }

    public void setParcela(double parcela) {
        this.parcela = parcela;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public double getTotalJuros() {
        return totalJuros;
    }

    public void setTotalJuros(double totalJuros) {
        this.totalJuros = totalJuros;
    }
}
