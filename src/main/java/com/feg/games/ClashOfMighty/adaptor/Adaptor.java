package com.feg.games.ClashOfMighty.adaptor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feg.games.ClashOfMighty.adaptor.dto.BetRequest;
import com.feg.games.ClashOfMighty.adaptor.dto.GameInitResponse;
import com.feg.games.ClashOfMighty.adaptor.dto.GamePlayResponse;
import com.feg.games.ClashOfMighty.adaptor.dto.MathState;
import com.feg.games.ClashOfMighty.engine.bonus.RngGameBonus;
import com.feg.games.ClashOfMighty.engine.service.DefaultGameEngine;
import com.feg.games.ClashOfMighty.ext.api.exception.GameEngineException;
import com.feg.games.ClashOfMighty.ext.api.model.GamePlay;
import com.feg.games.ClashOfMighty.ext.api.model.GameStatus;
import com.feg.games.ClashOfMighty.ext.slots.api.dto.SlotsGameEngineResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Adaptor {

    @Autowired
    private DefaultGameEngine gameEngine;
    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void postConstructActivity(){
        objectMapper.registerModule(gameEngine.getGameEngineModule());
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public GameInitResponse initialize(MathState intiRequest){
        GameInitResponse gameInitResponse = new GameInitResponse();
        gameInitResponse.setMathState(null);
        gameInitResponse.setGameData(gameEngine.getGameConfigurationJson());
        return gameInitResponse;
    }


    public GamePlayResponse playGame(JsonNode requestJson) {

        BetRequest betRequest = objectMapper.convertValue(requestJson, BetRequest.class);
        GamePlayResponse gamePlayResponse = new GamePlayResponse();

        GamePlay previousGamePlay = betRequest.getMathState().getGamePlayData();
        if(previousGamePlay == null || previousGamePlay.getStatus().equals(GameStatus.COMPLETED)) {
            SlotsGameEngineResponse slotsGameEngineResponse = new SlotsGameEngineResponse();
            GamePlay gamePlay = gameEngine.startGame();
            slotsGameEngineResponse.setGamePlay(gamePlay);
            gameEngine.validate(betRequest.getGameData(), slotsGameEngineResponse);
            gameEngine.play(betRequest.getGameData(), slotsGameEngineResponse);
            gameEngine.processWinnings(betRequest.getGameData(), slotsGameEngineResponse);

            MathState mathState = new MathState();
            mathState.setGamePlayData(slotsGameEngineResponse.getGamePlay());
            gamePlayResponse.setMathState(mathState);
            gamePlayResponse.setGameData(slotsGameEngineResponse);
            gamePlayResponse.setWin(slotsGameEngineResponse.getTotalPayout());
            gamePlayResponse.setCashOut(slotsGameEngineResponse.getGamePlay().getGamePlayState().getGameStatus().equals(GameStatus.COMPLETED));

            return gamePlayResponse;
        } else throw new GameEngineException("Previous game is in progress.");
    }

    public GamePlayResponse continuePlayGame(JsonNode requestJson) {
        BetRequest betRequest = objectMapper.convertValue(requestJson, BetRequest.class);
        GamePlayResponse gamePlayResponse = new GamePlayResponse();

        GamePlay previousGamePlay = betRequest.getMathState().getGamePlayData();
        if((previousGamePlay != null || previousGamePlay.getStatus().equals(GameStatus.INPROGRESS))
                && previousGamePlay.getGamePlayState().getBonusContext().getBonusAwarded() != RngGameBonus.NONE) {
            SlotsGameEngineResponse slotsGameEngineResponse = new SlotsGameEngineResponse();
            slotsGameEngineResponse.setGamePlay(previousGamePlay);
            if(betRequest.getGameData().isBuyFeature()) throw new GameEngineException("Continue game can't be BuyFeature.");

            gameEngine.play(betRequest.getGameData(), slotsGameEngineResponse);
            gameEngine.processWinnings(betRequest.getGameData(), slotsGameEngineResponse);

            MathState mathState = new MathState();
            mathState.setGamePlayData(slotsGameEngineResponse.getGamePlay());
            gamePlayResponse.setMathState(mathState);
            gamePlayResponse.setGameData(slotsGameEngineResponse);
            gamePlayResponse.setWin(slotsGameEngineResponse.getTotalPayout());
            gamePlayResponse.setCashOut(slotsGameEngineResponse.getGamePlay().getGamePlayState().getGameStatus().equals(GameStatus.COMPLETED));
            return gamePlayResponse;
        } else throw new GameEngineException("No Continue game Found.");
    }
}
