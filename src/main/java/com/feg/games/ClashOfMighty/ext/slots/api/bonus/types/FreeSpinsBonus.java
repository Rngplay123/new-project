package com.feg.games.ClashOfMighty.ext.slots.api.bonus.types;

import com.feg.games.ClashOfMighty.ext.api.bonus.GameBonus;

public class FreeSpinsBonus implements GameBonus {

    public static GameBonus createInstance() {
        return new FreeSpinsBonus();
    }

    @Override
    public boolean continueGameRound() {
        return true;
    }

    @Override
    public String getName() {
        return "FREE_SPINS";
    }
}
