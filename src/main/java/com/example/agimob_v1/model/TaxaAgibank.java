package com.example.agimob_v1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



public class TaxaAgibank extends Taxa{

    public TaxaAgibank(){
        super.setTipo_taxa("Agibank");
        super.setValor_taxa(0.09/12);
    }
}
