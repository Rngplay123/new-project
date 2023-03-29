package com.feg.games.ClashOfMighty.ext.slots.model;

import com.feg.games.ClashOfMighty.ext.api.symbol.Symbol;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * The WinningPayWays.
 */
@Data
@NoArgsConstructor
public class PayWay extends SlotsPay {

    private Integer payWays;

    private List<Integer> reelSymbolCount;

    //private List<Integer> cartSymbolPositions;

    public PayWay(Symbol symbol,
                  BigDecimal multiplier,
                  int ways,
                  List<Integer> reelSymbolCount,
                  List<List<Integer>> reelSymbolPositions
    ) {
        super(symbol, multiplier, reelSymbolPositions, reelSymbolCount.size());
        this.payWays = ways;
        this.reelSymbolCount = reelSymbolCount;
        //this.cartSymbolPositions = cartSymbolPositions;
    }

    public PayWay(Symbol symbol,
                  BigDecimal multiplier,
                  BigDecimal bonusMultiplier,
                  int ways,
                  List<Integer> reelSymbolCount,
                  List<List<Integer>> reelSymbolPositions
                  // LinkedList<Integer> cartSymbolPositions
    ) {
        this(symbol, multiplier, ways, reelSymbolCount, reelSymbolPositions);
        this.setBonusMultiplier(bonusMultiplier);
    }
}
