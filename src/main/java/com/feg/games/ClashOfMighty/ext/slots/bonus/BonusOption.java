package com.feg.games.ClashOfMighty.ext.slots.bonus;

import com.feg.games.ClashOfMighty.ext.slots.utils.Weighted;
import com.feg.games.ClashOfMighty.ext.api.bonus.GameBonus;
import com.feg.games.ClashOfMighty.ext.api.exception.GameEngineException;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.List;

/**
 * The BonusOption.
 */
@Data
@NoArgsConstructor
public class BonusOption<TGameBonus extends GameBonus> implements Weighted {

    private Integer index;
    private TGameBonus bonusType;
    //weighted bonus multipliers
    private List<BonusMultiplier> bonusMultipliers = BonusMultiplier.defaultMultiplier();
    private BigDecimal bonusMultiplierPicked = BigDecimal.ZERO;
    private Integer bonusValue = 0;
    private int weight;

    public static <TGameBonus extends GameBonus> BonusOption<TGameBonus> findBonusOptionByBonus(@NonNull final Integer bonus,
                                                                                                List<BonusOption<TGameBonus>> bonusOptions)
            throws GameEngineException {
        return bonusOptions.stream().filter(option -> option.getBonusValue().equals(bonus))
                .findAny().orElseThrow();

    }

    @Override
    public Integer getWeight() {
        return weight;
    }

    @Override
    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public BigDecimal getBonusMultiplierPicked() {
        return bonusMultiplierPicked;
    }
}
