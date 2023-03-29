package com.feg.games.ClashOfMighty.ext.slots.api.bonus.types;

import com.feg.games.ClashOfMighty.ext.api.bonus.GameBonus;


public class CashBonus implements GameBonus {

    public static GameBonus createInstance() {
        return new CashBonus();
    }

    public boolean continueGameRound() {
        return false;
    }

    @Override
    public String getName() {
        return "CASH";
    }
}
