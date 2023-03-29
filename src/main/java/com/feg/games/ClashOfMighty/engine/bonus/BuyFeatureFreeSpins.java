package com.feg.games.ClashOfMighty.engine.bonus;

import com.feg.games.ClashOfMighty.ext.api.bonus.GameBonus;

public class BuyFeatureFreeSpins implements GameBonus {

    public BuyFeatureFreeSpins() {
    }

    public static GameBonus createInstance() {
        return new BuyFeatureFreeSpins();
    }

    @Override
    public boolean continueGameRound() {
        return true;
    }

    @Override
    public String getName() {
        return "BUY_FEATURE_FREE_SPINS";
    }
}
