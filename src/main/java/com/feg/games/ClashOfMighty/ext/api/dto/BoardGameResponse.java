package com.feg.games.ClashOfMighty.ext.api.dto;

import com.feg.games.ClashOfMighty.ext.api.model.GameEngineResponse;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BoardGameResponse extends GameEngineResponse {

    boolean turnCompleted;

    Integer diceRolled;

    BigDecimal winnings;
}
