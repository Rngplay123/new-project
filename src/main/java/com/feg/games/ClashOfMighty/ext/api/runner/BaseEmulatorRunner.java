package com.feg.games.ClashOfMighty.ext.api.runner;


import com.feg.games.ClashOfMighty.ext.api.exception.GameEngineException;
import com.feg.games.ClashOfMighty.ext.api.service.EmulatorRunner;
import com.feg.games.ClashOfMighty.ext.api.model.EmulatorResult;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@Data
@NoArgsConstructor
@Slf4j
public abstract class BaseEmulatorRunner implements EmulatorRunner {

    private ApplicationContext applicationContext;
    private Map<String, String> options = new HashMap<>();
    private Integer runs;
    private Integer batchSize;


    @Override
    public Flux<EmulatorResult> start(Integer runs, Integer batchSize, Map<String, String> options, CountDownLatch latch) {
        Scheduler scheduler = Schedulers.newParallel("batch-processor");
        this.options = options;
        //int parallel = Integer.parseInt(options.getOrDefault("parallel", String.valueOf(2)));
        boolean publish = Boolean.parseBoolean(options.getOrDefault("publish", "false"));

        return Flux.range(0, getRuns(runs))
                .window(getBatchSize(batchSize))
                .flatMapSequential(group ->
                                group
                                        .parallel()
                                        .runOn(scheduler)
                                        .flatMap(run -> {
                                            try {
                                                return this.emulateGamePlay(run)
                                                        .doOnNext(emulatorResult -> {
                                                            /*if (publish) {
                                                                String topic = options.getOrDefault("emulator-topic", "emulator." + getGameConfiguration() + "." + System.currentTimeMillis());
                                                                eventBus.emit(topic, emulatorResult);
                                                            } else {*/
                                                                latch.countDown();
                                                            //}
                                                        });
                                            } catch (GameEngineException e) {
                                                e.printStackTrace();
                                                return Mono.error(new GameEngineException("Game emulation failed", e));
                                            }
                                        })
                                        .sequential()
                                        .reduce(new EmulatorResult(),
                                                (accumulator, result) -> accumulator.accumulate(result, false, Boolean.parseBoolean(options.getOrDefault("report", "false"))))
                        , 1)
                .index()
                .map(tuple2 -> {
                    long index = tuple2.getT1() + 1;
                    long endRun = index * getBatchSize(batchSize);
                    EmulatorResult result = tuple2.getT2();
                    result.setBatch((endRun - getBatchSize(batchSize)) + "-" + endRun);

                    log.info("Batch Size: {}, RTP: {}", result.getBatch(), result.getRtp());
                    return result;
                });
    }

    private int getBatchSize(Integer batchSize) {
        if (batchSize != null)
            return batchSize;

        return 100_000;
    }

    private int getRuns(Integer runs) {
        if (runs != null)
            return runs;

        return 1_000_000; //1M
    }

}
