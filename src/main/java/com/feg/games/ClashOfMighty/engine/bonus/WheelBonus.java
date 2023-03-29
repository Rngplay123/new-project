package com.feg.games.ClashOfMighty.engine.bonus;

import com.feg.games.ClashOfMighty.ext.api.bonus.GameBonus;

public class WheelBonus implements GameBonus {

    public WheelBonus() {
    }

    public static GameBonus createInstance() {
        return new WheelBonus();
    }

    @Override
    public boolean continueGameRound() {
        return true;
    }

    @Override
    public String getName() {
        return "WHEEL_BONUS";
    }
}
