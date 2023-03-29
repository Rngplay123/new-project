package com.feg.games.ClashOfMighty.engine.model;

import com.feg.games.ClashOfMighty.ext.slots.utils.Weighted;
import lombok.Data;

import java.util.List;
@Data
public class BuyFeatureReelConfiguration implements Weighted {
    int index;
    int weight;
    List<Integer> value;

    @Override
    public Integer getWeight() {
        return this.weight;
    }

    @Override
    public void setWeight(Integer weight) {
        this.weight=weight;
    }
}
