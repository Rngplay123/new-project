package com.feg.games.ClashOfMighty.engine.model;

import com.feg.games.ClashOfMighty.ext.slots.utils.Weighted;
import lombok.Data;

@Data
public class WheelOption implements Weighted {

    int     index;
    int     weight;
    String  type;
    int     value;

    @Override
    public Integer getWeight() {
        return this.weight;
    }

    @Override
    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
