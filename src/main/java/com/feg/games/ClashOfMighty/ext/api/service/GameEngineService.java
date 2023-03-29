package com.feg.games.ClashOfMighty.ext.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.feg.games.ClashOfMighty.ext.api.exception.GameEngineException;
import com.feg.games.ClashOfMighty.ext.api.model.*;
import com.feg.games.ClashOfMighty.ext.api.module.GameEngineModule;

import java.util.List;

/**
 * An Interface exposes the core API's that needs to be implemented by all game engines.
 */
public interface GameEngineService<TRequest extends GameEngineRequest,
        TResponse extends GameEngineResponse> {

    List<String> supportedGameConfigurations();

    default String getGameConfigurationJson(String gameConfiguration) {
        return "{}";
    }

    TResponse validate(TRequest gameEngineRequest,
                       StakeSettings stakeSettings,
                       GamePlay gamePlay) throws GameEngineException;

    JsonNode validate(JsonNode gameEngineRequest,
                      JsonNode stakeSettings,
                      JsonNode gamePlay) throws GameEngineException;

    GamePlay startGame(String gameConfiguration) throws GameEngineException;

    TResponse play(TRequest request, GamePlay gamePlay) throws GameEngineException;

    JsonNode play(JsonNode request, JsonNode jsonNode) throws GameEngineException;

    TResponse processWinnings(TRequest gameEngineRequest, GamePlay gamePlay, GameActivity gameActivity) throws GameEngineException;

    JsonNode processWinnings(JsonNode gameEngineRequest, JsonNode gamePlay, JsonNode jsonNode) throws GameEngineException;

    GameEngineModule getGameEngineModule();
}
