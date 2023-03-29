package com.feg.games.ClashOfMighty.ext.slots.api.bonus.types;

import com.feg.games.ClashOfMighty.ext.api.bonus.GameBonus;

public class ReSpinBonus implements GameBonus {

    public static GameBonus createInstance() {
        return new ReSpinBonus();
    }

    public boolean continueGameRound() {
        return true;
    }

    @Override
    public String getName() {
        return "RE_SPIN";
    }
}
