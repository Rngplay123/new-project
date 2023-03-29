package com.feg.games.ClashOfMighty.ext.api.bonus;

import com.feg.games.ClashOfMighty.ext.api.model.GamePlay;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class BonusContext {

    GameBonus bonusAwarded;

    protected BonusContext() {
    }

    protected BonusContext(GameBonus bonusAwarded) {
        this.bonusAwarded = bonusAwarded;
    }

    public static <T extends BonusContext> T copyFromPreviousOrCreateNew(T previous, Function<T, T> copyFunc, Supplier<T> newInstance) {
        if (previous == null)
            return newInstance.get();
        else
            return copyFunc.apply(previous);

    }

    public abstract boolean updateGameBonusAwarded(GamePlay gamePlay);


    public GameBonus getBonusAwarded() {
        return bonusAwarded;
    }

    public void setBonusAwarded(GameBonus bonusAwarded) {
        this.bonusAwarded = bonusAwarded;
    }
}
