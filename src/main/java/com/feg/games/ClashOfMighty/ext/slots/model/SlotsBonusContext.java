package com.feg.games.ClashOfMighty.ext.slots.model;

import com.feg.games.ClashOfMighty.ext.slots.api.model.SlotsGamePlayState;
import com.feg.games.ClashOfMighty.ext.slots.bonus.BonusOption;
import com.feg.games.ClashOfMighty.ext.slots.bonus.FreeSpinsContext;

import com.feg.games.ClashOfMighty.ext.api.bonus.BonusContext;
import com.feg.games.ClashOfMighty.ext.api.bonus.GameBonus;
import com.feg.games.ClashOfMighty.ext.api.model.GamePlay;
import com.feg.games.ClashOfMighty.ext.api.symbol.Symbol;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.LinkedMultiValueMap;

import java.math.BigDecimal;
import java.util.List;

/**
 * The SlotsBonusContext.
 */
@Getter
@Setter
@NoArgsConstructor
public abstract class SlotsBonusContext extends BonusContext {
    private boolean skipWinnings = false;

    private FreeSpinsContext freeSpinsContext;

    private BigDecimal bonusMultiplier = BigDecimal.ONE;

    private BonusOption<? extends GameBonus> bonusOptionAwarded;

    private int[] lockedReels;

    private int[] expandingReels;

    private LinkedMultiValueMap<Integer, Symbol> lockedGridSymbols = new LinkedMultiValueMap<>();

    private LinkedMultiValueMap<Integer, Integer> lockedGridPositions = new LinkedMultiValueMap<>();

    private List<Integer> stackedWildsReels;

    private LinkedMultiValueMap<Integer, Integer> wildPositions;

    public SlotsBonusContext(GameBonus bonusAwarded) {
        super(bonusAwarded);
    }

    public SlotsBonusContext(SlotsBonusContext prevSlotsBonusContext) {
        if (prevSlotsBonusContext.getFreeSpinsContext() != null) {
            this.freeSpinsContext = new FreeSpinsContext(prevSlotsBonusContext.getFreeSpinsContext());
        }
        if (prevSlotsBonusContext.getBonusMultiplier() != null)
            this.bonusMultiplier = prevSlotsBonusContext.getBonusMultiplier();
        this.bonusOptionAwarded = prevSlotsBonusContext.getBonusOptionAwarded();
        this.lockedReels = prevSlotsBonusContext.getLockedReels();
        this.expandingReels = prevSlotsBonusContext.getExpandingReels();
        this.lockedGridSymbols = prevSlotsBonusContext.getLockedGridSymbols();
        this.lockedGridPositions = prevSlotsBonusContext.getLockedGridPositions();
        this.stackedWildsReels = prevSlotsBonusContext.getStackedWildsReels();
    }

    @Override
    public boolean updateGameBonusAwarded(GamePlay gamePlay) {
        SlotsGamePlayState slotsGamePlay = (SlotsGamePlayState) gamePlay.getGamePlayState();
        BonusContext bonusContext = slotsGamePlay.getBonusContext();

        GameBonus bonusAwarded = null;
        if (bonusContext != null && bonusContext.getBonusAwarded() != null) {
            if (!slotsGamePlay.getLastBonusAwarded().equals(bonusContext.getBonusAwarded()))
                slotsGamePlay.getBonusAwarded().add(bonusContext.getBonusAwarded());

            bonusAwarded = bonusContext.getBonusAwarded();
        }

        return (bonusAwarded != null && bonusAwarded.continueGameRound())
               || (bonusContext != null && ((SlotsBonusContext) bonusContext).getFreeSpinsContext() != null && ((SlotsBonusContext) bonusContext).getFreeSpinsContext().getFreeSpinsRemaining() != 0);

    }

}
