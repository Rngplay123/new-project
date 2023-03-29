package com.feg.games.ClashOfMighty.ext.slots.reels;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.feg.games.ClashOfMighty.ext.slots.model.SymbolLine;
import com.feg.games.ClashOfMighty.ext.api.symbol.Symbol;
import com.feg.games.ClashOfMighty.ext.api.symbol.SymbolType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


@Data
@NoArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
public final class SymbolGrid {
    int rows;
    int columns;
    private List<List<Integer>> reelRowWeights;
    private List<SlotReel<Symbol>> slotRefReels;
    private List<Integer> topReelIndex;
    private LinkedMultiValueMap<Integer, Integer> symbolIndexGrid;
    private LinkedMultiValueMap<Integer, Symbol> symbolGrid;
    private int[] lockedReels;
    private int[] expandingReels;
    private List<Integer> reelRowsPicked;

    public SymbolGrid(LinkedMultiValueMap<Integer, Integer> symbolsIndexGrid, LinkedMultiValueMap<Integer, Symbol> symbolGrid) {
        this.symbolIndexGrid = symbolsIndexGrid;
        this.symbolGrid = symbolGrid;

        this.lockedReels = new int[symbolGrid.size()];
        this.expandingReels = new int[symbolGrid.size()];
    }

    public SymbolGrid(int rows, int columns, LinkedMultiValueMap<Integer, Integer> symbolsIndexGrid, LinkedMultiValueMap<Integer, Symbol> symbolGrid) {
        this.rows = rows;
        this.columns = columns;
        this.symbolIndexGrid = symbolsIndexGrid;
        this.symbolGrid = symbolGrid;

        this.lockedReels = new int[symbolGrid.size()];
        this.expandingReels = new int[symbolGrid.size()];
    }


   /* public SymbolGrid(int rows, int columns,
                      List<Integer> topReelIndex) {
        this(rows, columns, topReelIndex, null);
    }

    public SymbolGrid(int rows, int columns,
                      List<Integer> topReelIndex,
                      List<Integer> reelRowsPicked) {
        this.rows = rows;
        this.columns = columns;
        this.topReelIndex = topReelIndex;
        //this.reelRowWeights = reelRowWeights;
        this.reelRowsPicked = reelRowsPicked;
    }*/

    public SymbolGrid(SymbolGrid symbolGrid) {
        this.rows = symbolGrid.getRows();
        this.columns = symbolGrid.getColumns();
        this.symbolIndexGrid = cloneSymbolIndexGrid(symbolGrid.getSymbolIndexGrid());
        this.symbolGrid = cloneSymbolGrid(symbolGrid.symbolGrid);
        if(symbolGrid.getLockedReels()!=null)
            this.lockedReels = symbolGrid.getLockedReels().clone();
        if(symbolGrid.getExpandingReels()!=null)
            this.expandingReels = symbolGrid.getExpandingReels().clone();
    }

    private LinkedMultiValueMap<Integer, Integer> cloneSymbolIndexGrid(LinkedMultiValueMap<Integer, Integer> actual) {
        LinkedMultiValueMap<Integer, Integer> copy = new LinkedMultiValueMap<>();

        for (int reel = 0; reel < actual.size(); reel++) {
            for (int pos = 0; pos < actual.get(reel).size(); pos++) {
                copy.add(reel, actual.get(reel).get(pos));
            }
        }
        return copy;
    }

    private LinkedMultiValueMap<Integer, Symbol> cloneSymbolGrid(LinkedMultiValueMap<Integer, Symbol> actual) {
        LinkedMultiValueMap<Integer, Symbol> copy = new LinkedMultiValueMap<>();

        for (int reel = 0; reel < actual.size(); reel++) {
            for (int pos = 0; pos < actual.get(reel).size(); pos++)
                copy.add(reel, Objects.requireNonNull(actual.get(reel)).get(pos));
        }
        return copy;
    }

    /*private void populateReels() {
        int reel = 0;
        for (Integer topIndex : this.topReelIndex) {
            SlotReel<TSymbol> slotReel = slotRefReels.get(reel);
            populateReel(reel, topIndex, slotReel);
            reel++;
        }
    }
*/
   /* private void populateReel(int reel, int topSymbolIndex, SlotReel<TSymbol> slotReel) {
        List<Integer> reelIndexes;
        int reelRows = getRows(reel, slotReel);
        reelIndexes = NumberUtils.getSequenceList(topSymbolIndex, reelRows, slotReel.getSymbols().size());
        this.reelSymbolIndexes.put(reel, reelIndexes);
        List<TSymbol> symbolIndexes = new ArrayList<>(reelIndexes.size());

        List<TSymbol> slotReelSymbols = slotReel.getSymbols();

        reelIndexes.forEach(index -> symbolIndexes.add(slotReelSymbols.get(index)));
        reelSymbols.put(reel, symbolIndexes);
    }
*/
    public int getSymbolVisibleCountOnGrid(Symbol symbol) {
        int count = 0;
        for (int reel = 0; reel < getSymbolGrid().size(); reel++) {
            for (int pos = 0; pos < Objects.requireNonNull(getSymbolGrid().get(reel)).size(); pos++) {
                if (Objects.requireNonNull(getSymbolGrid().get(reel)).get(pos).equals(symbol)) {
                    count++;
                }
            }
        }
        return count;
    }

    public Integer getSymbolCount(List<List<Integer>> reelSymbolPositions) {
        if (CollectionUtils.isEmpty(reelSymbolPositions))
            return 0;

        AtomicInteger scatterCount = new AtomicInteger();
        reelSymbolPositions.forEach(reels -> {
            reels.forEach(positions -> {
                scatterCount.incrementAndGet();
            });
        });

        return scatterCount.get();
    }


    public List<List<Integer>> getSymbolPositionsOnGrid(Symbol symbol) {
        return getSymbolPositionsOnGrid(symbol, new ArrayList<>(0));
    }

    public List<List<Integer>> getSymbolPositionsOnGrid(Symbol symbol, List<Symbol> wilds) {
        List<List<Integer>> list = new ArrayList<>();
        for (int reel = 0; reel < getReelCount(); reel++) {
            list.add(getSymbolPositionsOnReel(symbol, reel, wilds));
        }
        return list;
    }

    public List<Integer> getSymbolPositionsOnReel(Symbol symbol, int reel, List<? extends Symbol> wilds) {
        List<Integer> symbolsPositions = new ArrayList<>();
        for (int pos = 0; pos < Objects.requireNonNull(getSymbolGrid().get(reel)).size(); pos++) {
            Symbol current = Objects.requireNonNull(getSymbolGrid()
                    .get(reel)).get(pos);
            if (current.equals(symbol) || wilds.contains(current)) {
                symbolsPositions.add(pos);
            }
        }
        return symbolsPositions;
    }


    public SymbolLine extractSymbolLineFromPayLineOffsets(int[] offsets) {

        SymbolLine symbolLine = new SymbolLine(offsets);
        for (int reel = 0; reel < offsets.length; reel++) {
            symbolLine.append(symbolGrid.get(reel)
                    .get(offsets[reel]));
        }
        return symbolLine;
    }

    public SymbolGrid expandSymbolsOnGrid(Symbol expandingSymbol) {
        SymbolGrid modified = new SymbolGrid(this);
        LinkedMultiValueMap<Integer, Symbol> reelSymbols = modified.getSymbolGrid();
        for (int reel = 0; reel < reelSymbols.size(); reel++) {
            for (int pos = 0; pos < reelSymbols.get(reel).size(); pos++) {
                if (Objects.requireNonNull(modified.getSymbolGrid().get(reel))
                        .get(pos).equals(expandingSymbol)) {
                    modified.replaceSymbolsOnReel(reel, expandingSymbol);
                    modified.expandingReels[reel] = 1;
                    break;
                }
            }
        }
        return modified;
    }

    public void replaceSymbolsOnReel(int reel, Symbol replacingSymbol) {
        for (int pos = 0; pos < Objects.requireNonNull(this.getSymbolGrid().get(reel)).size(); pos++) {
            Objects.requireNonNull(this.getSymbolGrid().get(reel))
                    .set(pos, replacingSymbol);
        }
    }


    @SuppressWarnings("ConstantConditions")
    public SymbolGrid replaceSymbolsOnGrid(Symbol from, Symbol to, boolean copy) {
        SymbolGrid symbolGrid = null;
        if (copy)
            new SymbolGrid(this);
        else
            symbolGrid = this;

        if (symbolGrid == null)
            return null;

        for (int reel = 0; reel < symbolGrid.getSymbolGrid().size(); reel++) {
            for (int pos = 0; pos < symbolGrid.getSymbolGrid().get(reel).size(); pos++) {
                if (symbolGrid.getSymbolGrid().get(reel).get(pos).equals(from))
                    symbolGrid.getSymbolGrid().get(reel).set(pos, to);
            }
        }

        return symbolGrid;
    }


    @SuppressWarnings("ConstantConditions")
    public Set<Symbol> extractDistinctSymbols(int reelIndex, boolean includeSpecialSymbols) {
        Set<Symbol> distinctSymbols = new HashSet<>();
        for (int rowIndex = 0; rowIndex < getSymbolGrid().get(reelIndex).size(); rowIndex++) {
            Symbol symbol = getSymbolGrid().get(reelIndex).get(rowIndex);
            if (symbol.getSymbolType().contains(SymbolType.SPECIAL) && !includeSpecialSymbols) {
                continue;
            }
            distinctSymbols.add(symbol);
        }
        return distinctSymbols;
    }


    @SuppressWarnings("ConstantConditions")
    public Symbol getSymbolByReelAndPosition(int reel, int position) {
        return getSymbolGrid().get(reel).get(position);
    }

    @SuppressWarnings("ConstantConditions")
    public void setSymbolByReelAndPosition(int reel, int position, Symbol symbol) {
        this.getSymbolGrid().get(reel).set(position, symbol);
    }

    public int getReelCount() {
        return getSymbolGrid().size();
    }

    public int getRowCount(int reel) {
        return getSymbolGrid().get(reel).size();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("\n");
        for (int j = 0; j < symbolGrid.get(0).size(); j++) {
            for (int i = 0; i < symbolGrid.size(); i++) {
                sb.append(symbolGrid.get(i).get(j).getCode()).append( (i==symbolGrid.size()-1)?"":",");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
