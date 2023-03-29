package com.feg.games.ClashOfMighty.ext.api.runner;

import com.feg.games.ClashOfMighty.ext.api.service.EmulatorRunner;
import com.feg.games.ClashOfMighty.ext.api.model.EmulatorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
public class EmulatorCommandLineRunner implements ApplicationContextAware {

    private ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }

    public void run(String... args) throws Exception {

        long start = System.currentTimeMillis();

        Map<String, String> options = new HashMap<>();
        String[] params = null;
        for (String arg : args) {
            params = arg.split("=");
            options.put(params[0], params[1]);
        }

        options.putIfAbsent("noOfRuns", "10_000_000");
        options.putIfAbsent("batchSize", "1_000_000");

        EmulatorRunner emulatorRunner = ctx.getBean(EmulatorRunner.class);

       /* log.info(System.lineSeparator() + System.lineSeparator() + "Sample command: " +
                "java -jar game-engine-<engine>-boot.jar noOfRuns=10_000_000 batchSize=1_000_000 gc=" + emulatorRunner.getGameConfiguration() + System.lineSeparator());
*/

        Integer runs = Integer.valueOf(options.get("noOfRuns").replaceAll("_", ""));
        Integer batchSize = Integer.valueOf(options.get("batchSize").replaceAll("_", ""));

        CountDownLatch latch = new CountDownLatch(runs);
        if (Math.floorMod(runs, batchSize) != 0) {
            throw new IllegalArgumentException("batchSize " + batchSize + " invalid");
        }

        log.info("Emulating game {}." + System.lineSeparator() +
                " Args = {}", options.getOrDefault("gc", emulatorRunner.getGameConfiguration()), options);

        EmulatorResult result = emulatorRunner.start(
                runs,
                batchSize,
                options,
                latch)
                .reduce((ac, r) -> ac.accumulate(r, true, Boolean.parseBoolean(options.getOrDefault("report", "false"))))
                .doOnNext(finalResult -> {
                    finalResult.setRun(runs);
                    finalResult.setBatchSize(batchSize);
                    BigDecimal RTP = finalResult.getWinnings()
                            .divide(finalResult.getStake(), MathContext.DECIMAL128)
                            .multiply(BigDecimal.valueOf(100)).setScale(4, RoundingMode.HALF_UP);

                    log.info("RTP: {}, emulator result {}", RTP, finalResult);
                }).block();

            long end = System.currentTimeMillis();
            log.info("Time taken for the emulation {}Secs", Duration.ofMillis(end - start).toSeconds());

            try {
                latch.await(60, TimeUnit.SECONDS);
            } catch (InterruptedException ignored) {
                log.warn("Latch timeout");
            }
            System.exit(0);
    }
}
