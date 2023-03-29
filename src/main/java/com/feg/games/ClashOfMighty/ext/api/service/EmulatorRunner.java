package com.feg.games.ClashOfMighty.ext.api.service;

import com.feg.games.ClashOfMighty.ext.api.exception.GameEngineException;
import com.feg.games.ClashOfMighty.ext.api.model.EmulatorResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

public interface EmulatorRunner {

    Flux<EmulatorResult> start(Integer runs, Integer groupBy, Map<String, String> options, CountDownLatch latch);

    Mono<EmulatorResult> emulateGamePlay(Integer run) throws GameEngineException;

    String getGameConfiguration();
}
