package com.feg.games.ClashOfMighty.engine.model;

import com.feg.games.ClashOfMighty.engine.symbols.RngSymbol;
import com.feg.games.ClashOfMighty.ext.slots.utils.Weighted;
import lombok.Data;


@Data
public class FreeSpinSymbolOption implements Weighted {
    int index;
    int weight;
    RngSymbol symbol;

    @Override
    public Integer getWeight() {
        return this.weight;
    }

    @Override
    public void setWeight(Integer weight) {
        this.weight=weight;
    }
}
