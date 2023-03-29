package com.feg.games.ClashOfMighty.engine.bonus;


import com.feg.games.ClashOfMighty.ext.api.bonus.GameBonus;

public class FreeRespin implements GameBonus {

    public FreeRespin(){
    }

    public static GameBonus createInstance(){
        return new FreeRespin();
    }

    @Override
    public boolean continueGameRound() {
        return true;
    }

    @Override
    public String getName() {
        return "FREE_RESPIN";
    }
}
