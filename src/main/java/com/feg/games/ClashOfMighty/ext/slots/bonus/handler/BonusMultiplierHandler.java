package com.feg.games.ClashOfMighty.ext.slots.bonus.handler;

import java.math.BigDecimal;

@FunctionalInterface
public interface BonusMultiplierHandler {

    BigDecimal getBonusMultiplier();
}
