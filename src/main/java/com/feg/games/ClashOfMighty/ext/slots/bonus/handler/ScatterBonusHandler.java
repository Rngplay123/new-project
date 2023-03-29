package com.feg.games.ClashOfMighty.ext.slots.bonus.handler;


import com.feg.games.ClashOfMighty.ext.slots.config.SlotsBonusGameConfiguration;
import com.feg.games.ClashOfMighty.ext.api.bonus.GameBonus;
import com.feg.games.ClashOfMighty.ext.api.symbol.Symbol;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ScatterBonusHandler<TSymbol extends Symbol> {

    public BigDecimal awardBonus(int scatterCount,
                                 SlotsBonusGameConfiguration<TSymbol, ? extends GameBonus> bgc){

        Integer[] scatterMultipliers = bgc.getScatterStakeMultipliers();

        int scatterMultiplier = 0;
        if (scatterMultipliers != null
                && scatterMultipliers.length >= scatterCount
                && scatterCount > 0) {
            scatterMultiplier = scatterMultipliers[scatterCount - 1];

            return BigDecimal.valueOf(scatterMultiplier);
        }

        return null;
    }
}
