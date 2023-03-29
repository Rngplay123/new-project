package com.feg.games.ClashOfMighty.ext.slots.bonus.handler;

import com.feg.games.ClashOfMighty.ext.slots.model.SymbolLine;

import java.math.BigDecimal;

@FunctionalInterface
public interface PayLineBonusMultiplierHandler {

    BigDecimal getBonusMultiplier(SymbolLine symbolLine, SymbolLine modifiedLine);
}
