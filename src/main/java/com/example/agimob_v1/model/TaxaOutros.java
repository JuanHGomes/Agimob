package com.example.agimob_v1.model;

public class TaxaOutros extends Taxa{

    public TaxaOutros(){
        super.setTipo_taxa("Outros");
        super.setValor_taxa(0.12/12);
    }
}
