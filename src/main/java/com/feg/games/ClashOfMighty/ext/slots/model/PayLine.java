package com.feg.games.ClashOfMighty.ext.slots.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.feg.games.ClashOfMighty.ext.api.symbol.Symbol;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;

/**
 * The PayLine.
 */
@Data
@NoArgsConstructor

public class PayLine extends SlotsPay {
    int payLine;
    SymbolLine initSymbolLine;
    SymbolLine paySymbolLine;
    int[] payLineOffSet;
    boolean wildContributingPayLine;

    public PayLine(Symbol symbol,
                   int payLine,
                   int[] payLineOffSet,
                   SymbolLine initSymbolLine,
                   SymbolLine paySymbolLine,
                   BigDecimal multiplier, Integer symbolCount) {
        super(symbol, multiplier, symbolCount);
        this.payLine = payLine;
        this.payLineOffSet = payLineOffSet;
        this.initSymbolLine = initSymbolLine;
        this.paySymbolLine = paySymbolLine;
        this.wildContributingPayLine = paySymbolLine.isWildContributingToWins(initSymbolLine, true);
    }

    public PayLine(Symbol symbol,
                   int payLine,
                   int[] payLineOffSet,
                   SymbolLine initSymbolLine,
                   SymbolLine paySymbolLine,
                   BigDecimal multiplier,
                   @NonNull BigDecimal bonusMultiplier, Integer symbolCount) {
        super(symbol, multiplier, symbolCount);
        this.payLine = payLine;
        this.payLineOffSet = payLineOffSet;
        this.initSymbolLine = initSymbolLine;
        this.paySymbolLine = paySymbolLine;
        this.setBonusMultiplier(bonusMultiplier);
        this.wildContributingPayLine = paySymbolLine.isWildContributingToWins(initSymbolLine, true);
    }

}
