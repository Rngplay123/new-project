package com.feg.games.ClashOfMighty.engine.bonus;


import com.feg.games.ClashOfMighty.ext.api.bonus.GameBonus;

public class BuyFeature implements GameBonus {

    public BuyFeature() {
    }

    public static GameBonus createInstance() {
        return new BuyFeature();
    }

    @Override
    public boolean continueGameRound() {
        return false;
    }

    @Override
    public String getName() {
        return "BUY_FEATURE";
    }
}
