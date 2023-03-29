package com.feg.games.ClashOfMighty.ext.slots.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.feg.games.ClashOfMighty.ext.slots.config.SlotsReelConfiguration;
import com.feg.games.ClashOfMighty.ext.slots.reels.CascadeDirection;
import com.feg.games.ClashOfMighty.ext.slots.reels.ReelLayout;
import com.feg.games.ClashOfMighty.ext.slots.reels.SlotReel;
import com.feg.games.ClashOfMighty.ext.slots.reels.SymbolGrid;
import com.feg.games.ClashOfMighty.ext.slots.utils.NumberUtils;
import com.feg.games.ClashOfMighty.ext.slots.pays.CascadePayStep;
import com.feg.games.ClashOfMighty.ext.slots.pays.PayStep;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.feg.games.ClashOfMighty.ext.api.bonus.BonusContext;
import com.feg.games.ClashOfMighty.ext.api.bonus.GameBonus;
import com.feg.games.ClashOfMighty.ext.api.exception.GameEngineException;
import com.feg.games.ClashOfMighty.ext.api.model.GameStatus;
import com.feg.games.ClashOfMighty.ext.api.model.SpinGameActivity;
import com.feg.games.ClashOfMighty.ext.api.symbol.Symbol;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The SlotsSpinGameActivity.
 */
@Data
@NoArgsConstructor
public class SlotsSpinGameActivity extends SpinGameActivity {

    private int reels;

    private int rows;

    private List<Integer> reelRows;

    private Integer selectedReel;

    private List<PayStep> paySteps;

    private GameBonus bonusAwarded;

    private Map<GameBonus, Symbol> specialSymbols;

    public SlotsSpinGameActivity(
            int reels,
            int rows,
            List<Integer> topReelIndexes,
            GameStatus gameStatus,
            Integer selectedReel,
            BonusContext bonusContext
    ) {

        super(gameStatus, topReelIndexes, bonusContext);
        this.reels = reels;
        this.rows = rows;
        this.selectedReel = selectedReel;
        //this.addPayStep();
    }

    public SlotsSpinGameActivity(
            int reels,
            int rows,
            List<Integer> reelRows,
            List<Integer> topReelIndexes,
            GameStatus gameStatus,
            Integer selectedReel,
            BonusContext bonusContext
    ) {
        this(reels, rows, topReelIndexes, gameStatus, selectedReel, bonusContext);
        this.reelRows = reelRows;
    }

    public SymbolGrid prepareSymbolGrid(SlotsReelConfiguration<? extends Symbol, ? extends GameBonus> reelConfiguration) {

        LinkedMultiValueMap<Integer, Integer> symbolIndexGrid = getSymbolIndexGrid(reelConfiguration.getReelLayouts().get(selectedReel));
        return new SymbolGrid(this.getRows(), this.reels, symbolIndexGrid, getSymbolGrid(symbolIndexGrid, reelConfiguration.getReelLayouts().get(selectedReel)));
    }

    public void addPayStep(PayStep payStep) {
        if (this.paySteps == null)
            this.paySteps = new LinkedList<>();

        this.paySteps.add(payStep);
    }

    @JsonIgnore
    public PayStep getLastPayStep() {
        return getPaySteps().get(this.paySteps.size() - 1);
    }

    private LinkedMultiValueMap<Integer, Integer> getSymbolIndexGrid(ReelLayout<? extends Symbol,
                                ? extends GameBonus> reelLayout) {
        LinkedMultiValueMap<Integer, Integer> symbolIndexGrid = new LinkedMultiValueMap<>();

        for (int reel = 0; reel < getSpinPositions().size(); reel++) {
            int rp = getSpinPositions().get(reel);
            int draw = getRows(reel, rows);
            int reelSize = reelLayout.getReels().get(reel).getSymbols().size();
            symbolIndexGrid.put(reel, NumberUtils.getSequenceList(rp, draw, reelSize));
        }
        return symbolIndexGrid;
    }


    private LinkedMultiValueMap<Integer, Symbol> getSymbolGrid(LinkedMultiValueMap<Integer, Integer> symbolIndexGrid,
                                                               ReelLayout<? extends Symbol, ? extends GameBonus> reelLayout) {
        LinkedMultiValueMap<Integer, Symbol> symbolGrid = new LinkedMultiValueMap<>();
        List<? extends SlotReel<? extends Symbol>> slotReels = reelLayout.getReels();

        symbolIndexGrid.forEach((reel, symbolIndexPositions) ->
                symbolIndexPositions.forEach(position ->
                        symbolGrid.add(reel, slotReels.get(reel).getSymbols().get(position))));
        return symbolGrid;
    }


    private int getRows(int reel, int defaultRows) {
        if (this.reelRows != null)
            return this.reelRows.get(reel);
        else
            return defaultRows;
    }

    public SymbolGrid addCascadePayStep(SlotsPay.PayType type,
                                        SlotsReelConfiguration<? extends Symbol, ? extends GameBonus> reelConfiguration) {

        if (!(getLastPayStep() instanceof CascadePayStep))
            return null;

        CascadePayStep payStep = (CascadePayStep) getLastPayStep();
        LinkedMultiValueMap<Integer, Integer> reactingSymbolIndexesOnReel = payStep.getReactingSymbolGrid();

        if (reactingSymbolIndexesOnReel == null)
            return null;

        //PayStep newPayStep = new CascadePayStep(new SymbolGrid(payStep.getStepSymbolGrid()));
        SymbolGrid afterStepGrid = new SymbolGrid(payStep.getStepSymbolGrid());
        reactingSymbolIndexesOnReel.forEach((reel, reactingSymbolIndexes) -> {

            if (CollectionUtils.isEmpty(reactingSymbolIndexes)) {
                return;
            }


            int rows = payStep.getStepSymbolGrid().getRowCount(reel);

            for (int rowPosition :
                    reactingSymbolIndexes) {
                if (rowPosition >= rows)
                    throw new UnsupportedOperationException(
                            "Invalid Cascade position (" + rowPosition
                            + ") greater than or equal to reel rows (" + rows + ")");
            }


            SlotReel<? extends Symbol> slotReel = reelConfiguration.getReelLayouts().get(selectedReel).getReels().get(reel);
            int reelSizeFromReelLayout = slotReel.getSymbols().size();

            int cascadePosition = 0;

            if (slotReel.getCascade()== CascadeDirection.TOP) {
                cascadePosition = getTopIndex(reel, afterStepGrid);
            } else if (slotReel.getCascade()== CascadeDirection.BOTTOM) {
                cascadePosition = getBottomIndex(reel, afterStepGrid, afterStepGrid.getRowCount(reel));
            }

            int removed = 0;
            for (int currentPos : reactingSymbolIndexes) {
                Objects.requireNonNull(afterStepGrid.getSymbolIndexGrid().get(reel)).set(currentPos, -1);
                Objects.requireNonNull(afterStepGrid.getSymbolGrid().get(reel)).set(currentPos, null);
                removed++;
            }

            Objects.requireNonNull(afterStepGrid.getSymbolIndexGrid().get(reel)).removeIf(index -> index == -1);
            Objects.requireNonNull(afterStepGrid.getSymbolGrid().get(reel)).removeIf(Objects::isNull);


            AtomicInteger currentIndex = new AtomicInteger(cascadePosition);
            while (removed != 0) {
                int nextReelIndex = nextReelIndex(currentIndex, slotReel.getCascade(), reelSizeFromReelLayout);

                currentIndex.set(nextReelIndex);
                addSymbolForSymbolIndex(reel, nextReelIndex, slotReel.getCascade(), afterStepGrid, reelConfiguration);
                removed--;
            }

            //this.resetTopReelIndex();

        });

        return afterStepGrid;
    }


    private int getTopIndex(int reel, SymbolGrid symbolGrid) {
        return Objects.requireNonNull(symbolGrid.getSymbolIndexGrid().get(reel)).get(0);
    }

    private int getBottomIndex(int reel, SymbolGrid symbolGrid, int reelSize) {
        return Objects.requireNonNull(symbolGrid.getSymbolIndexGrid().get(reel)).get(reelSize - 1);
    }

    @SuppressWarnings("SameParameterValue")
    private int nextReelIndex(AtomicInteger current, CascadeDirection cascade, int reelSize) {
        int currentPos;
        if (cascade == CascadeDirection.TOP)
            currentPos = current.decrementAndGet();
        else if (cascade == CascadeDirection.BOTTOM)
            currentPos = current.incrementAndGet();
        else {
            throw new GameEngineException("wrong Cascade direction used : " + cascade);
        }

        return Math.floorMod(currentPos, reelSize);
    }


    @SuppressWarnings("SameParameterValue")
    private void addSymbolForSymbolIndex(int reel,
                                         Integer newIndex,
                                         CascadeDirection cascadeDirection,
                                         SymbolGrid symbolGrid,
                                         SlotsReelConfiguration<? extends Symbol, ? extends GameBonus> reelConfiguration) {
        Symbol newSym = reelConfiguration.getReelLayouts().get(selectedReel).getReels().get(reel).getSymbols().get(newIndex);
        if (cascadeDirection == CascadeDirection.TOP) {
            Objects.requireNonNull(symbolGrid.getSymbolIndexGrid().get(reel)).add(0, newIndex);
            Objects.requireNonNull(symbolGrid.getSymbolGrid().get(reel)).add(0, newSym);
        } else {
            Objects.requireNonNull(symbolGrid.getSymbolIndexGrid().get(reel)).add(newIndex);
            Objects.requireNonNull(symbolGrid.getSymbolGrid().get(reel)).add(newSym);
        }
    }

    public <G extends GameBonus> G getBonusAwarded(Class<G> tClass) {
        return tClass.cast(bonusAwarded);
    }

    public void setBonusAwarded(GameBonus bonusAwarded) {
        this.bonusAwarded = bonusAwarded;
        this.getBonusContext().setBonusAwarded(bonusAwarded);
    }
}
