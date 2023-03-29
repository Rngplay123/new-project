package com.feg.games.ClashOfMighty.engine.bonus;


import com.feg.games.ClashOfMighty.ext.api.bonus.GameBonus;

public class BuyFeatureRespin implements GameBonus {

    public BuyFeatureRespin(){

    }

    public static GameBonus createInstance(){
        return new BuyFeatureRespin();
    }

    @Override
    public boolean continueGameRound() {
        return true;
    }

    @Override
    public String getName() {
        return "BUY_FEATURE_RESPIN";
    }
}
