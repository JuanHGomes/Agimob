package com.example.agimob_v1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ParcelaDto {
    private int numeroParcela;
    private double valorTotalParcela;
    private double valorJurosParcela;
    private double amortizacao;
    private double saldoDevedor;

}
