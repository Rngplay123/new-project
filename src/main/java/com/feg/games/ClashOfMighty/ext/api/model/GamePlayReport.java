package com.feg.games.ClashOfMighty.ext.api.model;

import java.math.BigDecimal;

public interface GamePlayReport {
    GamePlayReport accumulate(BigDecimal stake, GamePlayReport report, boolean group);
}
