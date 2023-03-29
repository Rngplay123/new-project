package com.feg.games.ClashOfMighty.ext.api.runner;


import com.feg.games.ClashOfMighty.ext.api.service.EmulatorRunner;
import com.feg.games.ClashOfMighty.ext.exceptions.SystemErrors;
import com.feg.games.ClashOfMighty.ext.exceptions.SystemRuntimeException;
import com.feg.games.ClashOfMighty.ext.api.model.EmulatorResult;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Map;

@Data
@Component
@Slf4j
public class EmulatorRunnerService implements ApplicationContextAware {


    private ApplicationContext applicationContext;
    private Flux<EmulatorResult> resultFlux;

    public Flux<EmulatorResult> startEmulator(Integer runs, Integer group, String gameConfiguration, Map<String, String> options) {
        if (resultFlux != null) {
            throw new SystemRuntimeException(SystemErrors.SYSTEM_ERROR);
        }

        EmulatorRunner emulatorRunner = (EmulatorRunner) applicationContext.getBean(gameConfiguration + "EmulatorRunner");
        resultFlux = emulatorRunner.start(runs, group, null, null);
        return resultFlux;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
