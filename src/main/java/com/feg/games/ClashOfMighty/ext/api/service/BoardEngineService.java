package com.feg.games.ClashOfMighty.ext.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.feg.games.ClashOfMighty.ext.api.exception.GameEngineException;
import com.feg.games.ClashOfMighty.ext.api.model.GameEngineRequest;
import com.feg.games.ClashOfMighty.ext.api.model.GameEngineResponse;
import com.feg.games.ClashOfMighty.ext.api.model.GamePlay;

public interface BoardEngineService<TGameRequest extends GameEngineRequest, TGameResponse extends GameEngineResponse> extends GameEngineService<TGameRequest, TGameResponse> {

    TGameResponse joinBoard(TGameRequest gameRequest, GamePlay gamePlay) throws GameEngineException;

    JsonNode joinBoard(JsonNode gameRequest, JsonNode gamePlayJsonNode) throws GameEngineException;

    TGameResponse rollDice(TGameRequest gameRequest, GamePlay gamePlay) throws GameEngineException;

    JsonNode rollDice(JsonNode gameRequest, JsonNode gamePlayJsonNode) throws GameEngineException;
}
