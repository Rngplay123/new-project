package com.feg.games.ClashOfMighty.ext.slots.api.bonus.types;

import com.feg.games.ClashOfMighty.ext.api.bonus.GameBonus;


public class StickyWildsBonus implements GameBonus {

    public static GameBonus createInstance() {
        return new StickyWildsBonus();
    }

    @Override
    public boolean continueGameRound() {
        return true;
    }

    @Override
    public String getName() {
        return "STICKY_WILDS";
    }
}
