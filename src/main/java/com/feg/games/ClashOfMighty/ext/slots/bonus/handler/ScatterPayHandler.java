package com.feg.games.ClashOfMighty.ext.slots.bonus.handler;

import com.feg.games.ClashOfMighty.ext.slots.model.SlotsPay;
import com.feg.games.ClashOfMighty.ext.slots.reels.SymbolGrid;
import com.feg.games.ClashOfMighty.ext.api.symbol.Symbol;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;


@Component
public class ScatterPayHandler {

    public SlotsPay awardBonus(SymbolGrid symbolGrid, Symbol scatterSymbol,
                               Integer[] scatterMultipliers) {
        List<List<Integer>> reelSymbolPositions = symbolGrid.getSymbolPositionsOnGrid(scatterSymbol);
        Integer scatterCount = symbolGrid.getSymbolCount(reelSymbolPositions);
        int scatterMultiplier = scatterCount > 0 ? scatterMultipliers[scatterCount - 1] : 0;
        return new SlotsPay(scatterSymbol,
                BigDecimal.valueOf(scatterMultiplier), reelSymbolPositions, scatterCount);
    }
}
