package com.feg.games.ClashOfMighty.ext.slots.bonus.handler;

import com.feg.games.ClashOfMighty.ext.slots.model.PayLine;
import com.feg.games.ClashOfMighty.ext.slots.model.SymbolLine;
import com.feg.games.ClashOfMighty.ext.api.symbol.Symbol;

import java.math.BigDecimal;

public interface PayLineHandler {
    PayLine getWinningLine(int payLine,
                           int[] payLineOffset,
                           Symbol paySymbol,
                           SymbolLine symbolLine,
                           BigDecimal initLineMultiplier,
                           SymbolLine modifiedLine,
                           BigDecimal modifiedLineMultiplier,
                           PayLineBonusMultiplierHandler payLineBonusMultiplierHandler);
}
