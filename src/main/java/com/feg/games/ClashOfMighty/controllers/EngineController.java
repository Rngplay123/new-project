package com.feg.games.ClashOfMighty.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.feg.games.ClashOfMighty.adaptor.Adaptor;
import com.feg.games.ClashOfMighty.adaptor.dto.GameInitResponse;
import com.feg.games.ClashOfMighty.adaptor.dto.GamePlayResponse;
import com.feg.games.ClashOfMighty.adaptor.dto.MathState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("MyGame/game-math")
public class EngineController {
    @Autowired
    private Adaptor adaptor;

    @PostMapping("/startup-data")
    public GameInitResponse startupMethod(@RequestBody MathState initRequest) {
        return adaptor.initialize(initRequest);
    }

    @PostMapping("/bet-data")
    public GamePlayResponse betMethod(@RequestBody JsonNode requestJson) {
        return adaptor.playGame(requestJson);
    }

    @PostMapping("/step-math")
    public GamePlayResponse continueGame(@RequestBody JsonNode requestJson) {
        return adaptor.continuePlayGame(requestJson);
    }

}
