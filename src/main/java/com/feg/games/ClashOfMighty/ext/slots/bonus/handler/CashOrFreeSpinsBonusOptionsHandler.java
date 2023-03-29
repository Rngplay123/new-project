package com.feg.games.ClashOfMighty.ext.slots.bonus.handler;

import com.feg.games.ClashOfMighty.ext.slots.config.SlotsBonusGameConfiguration;
import com.feg.games.ClashOfMighty.ext.slots.model.SlotsBonusContext;
import com.feg.games.ClashOfMighty.ext.slots.utils.PickByWeightage;
import com.feg.games.ClashOfMighty.ext.slots.api.bonus.types.CashBonus;
import com.feg.games.ClashOfMighty.ext.slots.api.bonus.types.FreeSpinsBonus;
import com.feg.games.ClashOfMighty.ext.slots.api.bonus.types.MultiplierBonus;
import com.feg.games.ClashOfMighty.ext.slots.bonus.BonusOption;
import com.feg.games.ClashOfMighty.ext.api.bonus.GameBonus;
import com.feg.games.ClashOfMighty.ext.api.symbol.Symbol;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("cashOrFreeSpinsBonusOptionsHandler")
@Data
@Slf4j
public class CashOrFreeSpinsBonusOptionsHandler<TSymbol extends Symbol, TGameBonus extends GameBonus>
        extends FreeSpinBonusHandler<TSymbol, TGameBonus> {

    @Autowired
    PickByWeightage pickByWeightage;

    public Map<BonusOption<TGameBonus>, List<BonusOption<TGameBonus>>> pickBonusOption(SlotsBonusContext slotsBonusContext, int scatterCount,
                                                                                       SlotsBonusGameConfiguration<TSymbol, TGameBonus> bc, List<BonusOption<TGameBonus>> bonusOptions, int noOfSpins) {

        GameBonus bonusAwarded = super.awardBonus(slotsBonusContext, scatterCount, bc.getFreeGameConfiguration());

        if (bonusAwarded == null)
            return null;

        List<BonusOption<TGameBonus>> optionsPicked = pickByWeightage.pickRandomNItems(bonusOptions, noOfSpins);

        BonusOption<TGameBonus> bonusAccumulated = optionsPicked
                .stream().reduce(new BonusOption<>(), (accumulator, option) -> {
                    if (option.getBonusType().getName().equals(FreeSpinsBonus.createInstance().getName())) {
                        accumulator.setBonusValue(accumulator.getBonusValue()
                                                  + option.getBonusValue());
                        accumulator.setBonusType(option.getBonusType());
                        accumulator.setIndex(option.getIndex());
                    } else if (option.getBonusType().getName().equals(CashBonus.createInstance().getName())) {
                        accumulator.setBonusValue(accumulator.getBonusValue()
                                                  + option.getBonusValue());
                        accumulator.setBonusType(option.getBonusType());
                        accumulator.setIndex(option.getIndex());
                    } else if (option.getBonusType().getName().equals(MultiplierBonus.get().getName())) {
                        accumulator.setIndex(option.getIndex());
                        accumulator
                                .setBonusMultiplierPicked(accumulator.getBonusMultiplierPicked()
                                        .add(BigDecimal.valueOf(option.getBonusValue())));
                    }
                    return accumulator;
                });

        Map<BonusOption<TGameBonus>, List<BonusOption<TGameBonus>>> result = new HashMap<>();
        result.put(bonusAccumulated, optionsPicked);
        return result;
    }
}
