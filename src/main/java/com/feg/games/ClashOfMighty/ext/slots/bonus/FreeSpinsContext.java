package com.feg.games.ClashOfMighty.ext.slots.bonus;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The FreeSpinsContext.
 */
@Data
@NoArgsConstructor
public class FreeSpinsContext {

    int freeSpinsAwarded;

    int freeSpinsRemaining;

    int totalFreeSpinsAwarded;

    public FreeSpinsContext(FreeSpinsContext bonusContext) {
        //this.freeSpinsAwarded = bonusContext.getFreeSpinsAwarded();
        this.totalFreeSpinsAwarded = bonusContext.getTotalFreeSpinsAwarded();
        this.freeSpinsRemaining = bonusContext.getFreeSpinsRemaining();
    }

    public static FreeSpinsContext awardFreeSpins(FreeSpinsContext freeSpinsContext, Integer freeSpinsAwarded) {
        if (freeSpinsContext == null) {
            freeSpinsContext = new FreeSpinsContext();
        }

        if (freeSpinsAwarded > 0) {
            freeSpinsContext.setFreeSpinsAwarded(freeSpinsAwarded);
            freeSpinsContext.setTotalFreeSpinsAwarded(freeSpinsContext.getTotalFreeSpinsAwarded() + freeSpinsAwarded);
            freeSpinsContext.setFreeSpinsRemaining(freeSpinsContext.getFreeSpinsRemaining() + freeSpinsAwarded);
        }
        return freeSpinsContext;
    }

    public void consumeFreeSpin() {
        if (this.freeSpinsRemaining > 0)
            this.freeSpinsRemaining--;
    }
}
