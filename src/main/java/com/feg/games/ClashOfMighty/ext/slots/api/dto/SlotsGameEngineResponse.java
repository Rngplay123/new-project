package com.feg.games.ClashOfMighty.ext.slots.api.dto;


import com.feg.games.ClashOfMighty.ext.api.model.GameEngineResponse;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SlotsGameEngineResponse extends GameEngineResponse {
    BigDecimal spinPayout;
}
