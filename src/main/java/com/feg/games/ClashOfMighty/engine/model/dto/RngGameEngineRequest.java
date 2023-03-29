package com.feg.games.ClashOfMighty.engine.model.dto;

import com.feg.games.ClashOfMighty.ext.slots.api.dto.SlotsGameEngineRequest;
import lombok.Data;

@Data
public class RngGameEngineRequest extends SlotsGameEngineRequest {

    boolean buyFeature;
}
