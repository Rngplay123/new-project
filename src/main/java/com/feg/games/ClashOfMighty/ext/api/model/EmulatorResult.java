package com.feg.games.ClashOfMighty.ext.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Data
@EqualsAndHashCode
public class EmulatorResult {
    Integer run;
    Integer batchSize;
    BigDecimal stake;
    BigDecimal winnings;
    Double maxExposure = 0D;
    BigDecimal rtp;
    String batch;
    GamePlayReport report;

    public EmulatorResult() {
        this.stake = BigDecimal.ZERO;
        this.winnings = BigDecimal.ZERO;
    }

    public EmulatorResult(BigDecimal stake, BigDecimal winnings) {
        this.stake = stake;
        this.winnings = winnings;
    }


    public EmulatorResult accumulate(EmulatorResult result, boolean batch,boolean reportNeeded) {
        BigDecimal acStake = this.getStake().add(result.getStake());
        BigDecimal acWins = this.getWinnings().add(result.getWinnings());
        this.setStake(acStake);
        this.setWinnings(acWins);
        if (!batch)
            this.maxExposure = Math.max(this.maxExposure, result.winnings.doubleValue());
        else
            this.maxExposure = Math.max(this.maxExposure, result.maxExposure);
        if (!batch)
            this.setBatch(result.getBatch());
        // if (group) {
        this.setRtp(this.getWinnings()
                .divide(this.getStake(), MathContext.DECIMAL128)
                .multiply(BigDecimal.valueOf(100)).setScale(4, RoundingMode.HALF_UP));
        //}

        if (reportNeeded) {
            if (this.report == null)
                this.report = result.getReport();
            else
                this.report.accumulate(this.getStake(), result.getReport(), batch);
        }

        return this;
    }
}
