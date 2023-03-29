package com.feg.games.ClashOfMighty.ext.api.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public abstract class GameEngineResponse {

    BigDecimal totalBet;

    BigDecimal totalPayout;

    GamePlay gamePlay;

    GameActivity gameActivity;
}
