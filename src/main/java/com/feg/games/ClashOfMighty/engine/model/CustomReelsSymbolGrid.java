package com.feg.games.ClashOfMighty.engine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.feg.games.ClashOfMighty.engine.symbols.RngSymbol;
import com.feg.games.ClashOfMighty.ext.api.bonus.BonusContext;
import com.feg.games.ClashOfMighty.ext.api.bonus.GameBonus;
import com.feg.games.ClashOfMighty.ext.api.model.GameStatus;
import com.feg.games.ClashOfMighty.ext.api.model.SpinGameActivity;
import com.feg.games.ClashOfMighty.ext.api.symbol.Symbol;
import com.feg.games.ClashOfMighty.engine.bonus.RngGameBonus;
import com.feg.games.ClashOfMighty.ext.slots.pays.PayStep;
import com.feg.games.ClashOfMighty.ext.slots.reels.ReelLayout;
import com.feg.games.ClashOfMighty.ext.slots.reels.SlotReel;
import com.feg.games.ClashOfMighty.ext.slots.reels.SymbolGrid;
import com.feg.games.ClashOfMighty.ext.slots.utils.NumberUtils;
import lombok.Data;
import org.springframework.util.LinkedMultiValueMap;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
public class CustomReelsSymbolGrid extends SpinGameActivity {
    private int reels;
    private int rows;
    private List<Integer> reelRows;
    private Integer selectedReel;
    private List<PayStep> paySteps;
    private GameBonus bonusAwarded;
    private Map<GameBonus, Symbol> specialSymbols;

    public CustomReelsSymbolGrid(int reels, int rows, List<Integer> topReelIndex, GameStatus gameStatus, BonusContext bonusContext) {
        super(gameStatus, topReelIndex, bonusContext);
        this.reels = reels;
        this.rows = rows;
    }

    public SymbolGrid prepareSymbolGrid(ReelLayout<RngSymbol, RngGameBonus> reelLayout) {
        LinkedMultiValueMap<Integer, Integer> symbolIndexGrid = this.getSymbolIndexGrid(reelLayout);
        return new SymbolGrid(this.getRows(), this.reels, symbolIndexGrid, this.getSymbolGrid(symbolIndexGrid,reelLayout));
    }

    public void addPayStep(PayStep payStep) {
        if (this.paySteps == null) {
            this.paySteps = new LinkedList();
        }

        this.paySteps.add(payStep);
    }

    @JsonIgnore
    public PayStep getLastPayStep() {
        return (PayStep)this.getPaySteps().get(this.paySteps.size() - 1);
    }

    private LinkedMultiValueMap<Integer, Integer> getSymbolIndexGrid(ReelLayout<? extends Symbol, ? extends GameBonus> reelLayout) {
        LinkedMultiValueMap<Integer, Integer> symbolIndexGrid = new LinkedMultiValueMap();

        for(int reel = 0; reel < this.getSpinPositions().size(); ++reel) {
            int rp = (Integer)this.getSpinPositions().get(reel);
            int draw = this.getRows(reel, this.rows);
            int reelSize = ((SlotReel)reelLayout.getReels().get(reel)).getSymbols().size();
            symbolIndexGrid.put(reel, NumberUtils.getSequenceList(rp, draw, reelSize));
        }

        return symbolIndexGrid;
    }

    private LinkedMultiValueMap<Integer, Symbol> getSymbolGrid(LinkedMultiValueMap<Integer, Integer> symbolIndexGrid, ReelLayout<RngSymbol, RngGameBonus> reelLayout) {
        LinkedMultiValueMap<Integer, Symbol> symbolGrid = new LinkedMultiValueMap();
        List<? extends SlotReel<? extends Symbol>> slotReels = reelLayout.getReels();
        symbolIndexGrid.forEach((reel, symbolIndexPositions) -> {
            symbolIndexPositions.forEach((position) -> {
                symbolGrid.add(reel, (Symbol)((SlotReel)slotReels.get(reel)).getSymbols().get(position));
            });
        });
        return symbolGrid;
    }

    private int getRows(int reel, int defaultRows) {
        return this.reelRows != null ? (Integer)this.reelRows.get(reel) : defaultRows;
    }

    public int getReels() {
        return this.reels;
    }

    public int getRows() {
        return this.rows;
    }

    public List<Integer> getReelRows() {
        return this.reelRows;
    }

    public Integer getSelectedReel() {
        return this.selectedReel;
    }

    public List<PayStep> getPaySteps() {
        return this.paySteps;
    }

    public GameBonus getBonusAwarded() {
        return this.bonusAwarded;
    }

    public Map<GameBonus, Symbol> getSpecialSymbols() {
        return this.specialSymbols;
    }

    public String toString() {
        int var10000 = this.getReels();
        return "SlotsSpinGameActivity(reels=" + var10000 + ", rows=" + this.getRows() + ", reelRows=" + this.getReelRows() + ", selectedReel=" + this.getSelectedReel() + ", paySteps=" + this.getPaySteps() + ", bonusAwarded=" + this.getBonusAwarded() + ", specialSymbols=" + this.getSpecialSymbols() + ")";
    }

    public CustomReelsSymbolGrid() {
    }
}
