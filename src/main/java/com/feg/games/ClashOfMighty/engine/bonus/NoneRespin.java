package com.feg.games.ClashOfMighty.engine.bonus;


import com.feg.games.ClashOfMighty.ext.api.bonus.GameBonus;

public class NoneRespin implements GameBonus {

    public NoneRespin(){
    }

    public static GameBonus createInstance(){
        return new NoneRespin();
    }

    @Override
    public boolean continueGameRound() {
        return true;
    }
    @Override
    public String getName() {
        return "NONE_RESPIN";
    }
}
