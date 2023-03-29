package com.feg.games.ClashOfMighty.ext.slots.api.bonus.types;

import com.feg.games.ClashOfMighty.ext.api.bonus.GameBonus;

public class NoneBonus implements GameBonus {

    public static GameBonus createInstance() {
        return new NoneBonus();
    }

    @Override
    public boolean continueGameRound() {
        return false;
    }

    @Override
    public String getName() {
        return "NONE";
    }
}
