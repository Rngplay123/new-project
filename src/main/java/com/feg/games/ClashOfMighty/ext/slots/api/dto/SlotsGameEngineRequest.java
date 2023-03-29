package com.feg.games.ClashOfMighty.ext.slots.api.dto;

import com.feg.games.ClashOfMighty.ext.api.model.GameEngineRequest;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SlotsGameEngineRequest extends GameEngineRequest {

    Integer noOfLines;

    BigDecimal stakePerLine;
}
