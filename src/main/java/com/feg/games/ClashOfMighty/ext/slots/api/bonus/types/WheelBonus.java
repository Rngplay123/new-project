package com.feg.games.ClashOfMighty.ext.slots.api.bonus.types;

import com.feg.games.ClashOfMighty.ext.api.bonus.GameBonus;


public class WheelBonus implements GameBonus {

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


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GameBonus))
            return false;

        return this.getName().equals(((GameBonus) obj).getName());
    }
}
