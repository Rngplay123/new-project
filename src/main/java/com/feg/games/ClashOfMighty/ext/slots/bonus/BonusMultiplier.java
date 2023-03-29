package com.feg.games.ClashOfMighty.ext.slots.bonus;

import com.feg.games.ClashOfMighty.ext.slots.utils.Weighted;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * The BonusMultiplier.
 */
@Data
@NoArgsConstructor
public class BonusMultiplier implements Weighted {
    BigDecimal multiplier;
    Integer weight;

    public BonusMultiplier(BigDecimal multiplier,
                           Integer weight) {
        this.multiplier = multiplier;
        this.weight = weight;
    }

    public static List<BonusMultiplier> defaultMultiplier() {
        return Collections
                .singletonList(new BonusMultiplier(BigDecimal.ONE, 0));
    }

    @Override
    public Integer getWeight() {
        return weight;
    }

    @Override
    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
