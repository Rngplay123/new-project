package com.feg.games.ClashOfMighty.ext.slots.symbol;

import com.feg.games.ClashOfMighty.ext.slots.model.SymbolLine;
import com.feg.games.ClashOfMighty.ext.api.symbol.Symbol;

@FunctionalInterface
public interface PaySymbolHandler {
    Symbol getPaySymbol(SymbolLine symbolLine);
}
