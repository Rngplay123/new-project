package com.feg.games.ClashOfMighty.ext.api.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.feg.games.ClashOfMighty.ext.api.exception.GameEngineException;
import com.feg.games.ClashOfMighty.ext.api.service.GameEngineService;
import com.feg.games.ClashOfMighty.ext.api.bonus.GameBonus;
import lombok.Data;

import java.util.concurrent.ConcurrentHashMap;


@Data
public abstract class ForceGameResult {

    private final ConcurrentHashMap<String, JsonNode> gamePlayUidToForcedResultMap = new ConcurrentHashMap<>();

    abstract public void play();

    @SuppressWarnings("rawtypes")
    public abstract GameEngineService getGameEngineService();

    public abstract GameEngineService setGameEngineService(GameEngineService gameEngineService);

    public abstract boolean applyForcedResult(JsonNode forcedResultBonus, GamePlay gamePlay) throws GameEngineException;

    public abstract GameBonus getPlayingGameBonus(GamePlay gamePlay) throws GameEngineException;

}
