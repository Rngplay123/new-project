package com.feg.games.ClashOfMighty.engine.model;

import com.feg.games.ClashOfMighty.ext.slots.utils.Weighted;
import lombok.Data;

@Data
public class WheelOptions implements Weighted {

    int index;

    int weight;

    String reward;

    @Override
    public Integer getWeight() {
        return this.weight;
    }

    @Override
    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
