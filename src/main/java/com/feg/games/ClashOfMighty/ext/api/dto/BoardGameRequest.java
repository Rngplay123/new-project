package com.feg.games.ClashOfMighty.ext.api.dto;

import com.feg.games.ClashOfMighty.ext.api.model.GameEngineRequest;
import lombok.Data;

@Data
public class BoardGameRequest extends GameEngineRequest {

    Integer pawn;
}
