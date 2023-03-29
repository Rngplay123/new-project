package com.feg.games.ClashOfMighty.engine.bonus;

import com.feg.games.ClashOfMighty.ext.api.bonus.GameBonus;
import com.feg.games.ClashOfMighty.ext.slots.api.bonus.types.FreeSpinsBonus;
import com.feg.games.ClashOfMighty.ext.slots.api.bonus.types.NoneBonus;

public enum RngGameBonus implements GameBonus {

    NONE(NoneBonus.createInstance()),
    FREE_SPINS(FreeSpinsBonus.createInstance()),
    NONE_RESPIN(NoneRespin.createInstance()),
    FREE_RESPIN(FreeRespin.createInstance()),
    BUY_FEATURE(BuyFeature.createInstance()),
    BUY_FEATURE_RESPIN(BuyFeatureRespin.createInstance()),
    BUY_FEATURE_FREE_SPINS(BuyFeatureFreeSpins.createInstance());
    GameBonus bonus;

    RngGameBonus(GameBonus bonus) {
        this.bonus = bonus;
    }

    @Override
    public boolean continueGameRound() {
        return this.bonus.continueGameRound();
    }

    @Override
    public String getName() {
        return this.bonus.getName();
    }
}