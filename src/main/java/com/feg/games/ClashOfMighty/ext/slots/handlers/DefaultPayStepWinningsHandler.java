package com.feg.games.ClashOfMighty.ext.slots.handlers;

import com.feg.games.ClashOfMighty.ext.slots.pays.PayStep;
import com.feg.games.ClashOfMighty.ext.slots.model.SlotsPay;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;

@Component
public class DefaultPayStepWinningsHandler {

    public BigDecimal payStepWinnings(BigDecimal totalBet, BigDecimal stakePerLine, Integer noOfLines, PayStep payStep) {
        BigDecimal scatterWins = BigDecimal.ZERO;

        if (payStep.getScatterPay() != null && payStep.getScatterPay().getMultiplier().compareTo(BigDecimal.ZERO) > 0) {
            scatterWins = payStep.getScatterPay().winsMultiplier().multiply(totalBet).setScale(SlotsPay.SCALE, SlotsPay.ROUNDING_MODE);
            payStep.getScatterPay().setWinnings(scatterWins);
        }


        // System.out.println(payStep.getPayLines().size());

        //.limit(gameEngineRequest.getNoOfLines())
        BigDecimal payLineWins = BigDecimal.ZERO;
        if (!CollectionUtils.isEmpty(payStep.getPayLines())) {

            payLineWins = payStep.getPayLines()
                    .stream()
                    //.limit(noOfLines)
                    .map(payLine -> {
                        payLine.setWinnings(payLine.winsMultiplier().multiply(stakePerLine)
                                .setScale(SlotsPay.SCALE, SlotsPay.ROUNDING_MODE));
                        return payLine.getWinnings();
                    })
                    .reduce(BigDecimal::add).get();
            //.add(scatterWins);//scatter wins
        }

        BigDecimal payWayWins = BigDecimal.ZERO;
        if (!CollectionUtils.isEmpty(payStep.getPayWays())) {
            payWayWins = payStep.getPayWays()
                    .stream()
                    //.limit(gameEngineRequest.getNoOfLines())
                    //ex:- (bet * 5 (symbol count multiplier) * 2 (payways)
                    .map(payWay -> {
                                payWay.setWinnings(totalBet
                                        .multiply(payWay.getMultiplier())// x symbol count multiplier
                                        .multiply(BigDecimal.valueOf(payWay.getPayWays()))
                                        .setScale(SlotsPay.SCALE, SlotsPay.ROUNDING_MODE));
                                return payWay.getWinnings();
                            }
                    )//x payways
                    .reduce(BigDecimal::add).get();
        }

        BigDecimal clusterWins = BigDecimal.ZERO;
        if (!CollectionUtils.isEmpty(payStep.getClusters())) {
            clusterWins = payStep.getClusters()
                    .stream()
                    .map(cluster -> {
                                cluster.setWinnings(cluster.winsMultiplier()
                                        .multiply(totalBet));
                                return cluster.getWinnings();
                            }
                    )
                    .reduce(BigDecimal::add).get();
        }

        payStep.setStepWins(scatterWins.add(payWayWins).add(payLineWins).add(clusterWins));
        return payStep.getStepWins();
    }

    public BigDecimal payStepWinnings(BigDecimal scatterPayStake, BigDecimal totalBet, BigDecimal stakePerLine, Integer noOfLines, PayStep payStep) {
        BigDecimal scatterWins = BigDecimal.ZERO;

        if (payStep.getScatterPay() != null && payStep.getScatterPay().getMultiplier().compareTo(BigDecimal.ZERO) > 0) {
            scatterWins = payStep.getScatterPay().winsMultiplier().multiply(scatterPayStake).setScale(SlotsPay.SCALE, SlotsPay.ROUNDING_MODE);
            payStep.getScatterPay().setWinnings(scatterWins);
        }
        // System.out.println(payStep.getPayLines().size());

        //.limit(gameEngineRequest.getNoOfLines())
        BigDecimal payLineWins = BigDecimal.ZERO;
        if (!CollectionUtils.isEmpty(payStep.getPayLines())) {

            payLineWins = payStep.getPayLines()
                    .stream()
                    //.limit(noOfLines)
                    .map(payLine -> {
                        payLine.setWinnings(payLine.winsMultiplier().multiply(stakePerLine)
                                .setScale(SlotsPay.SCALE, SlotsPay.ROUNDING_MODE));
                        return payLine.getWinnings();
                    })
                    .reduce(BigDecimal::add).get();
            //.add(scatterWins);//scatter wins
        }

        BigDecimal payWayWins = BigDecimal.ZERO;
        if (!CollectionUtils.isEmpty(payStep.getPayWays())) {
            payWayWins = payStep.getPayWays()
                    .stream()
                    //.limit(gameEngineRequest.getNoOfLines())
                    //ex:- (bet * 5 (symbol count multiplier) * 2 (payways)
                    .map(payWay -> {
                                payWay.setWinnings(totalBet
                                        .multiply(payWay.getMultiplier())// x symbol count multiplier
                                        .multiply(BigDecimal.valueOf(payWay.getPayWays()))
                                        .setScale(SlotsPay.SCALE, SlotsPay.ROUNDING_MODE));
                                return payWay.getWinnings();
                            }
                    )//x payways
                    .reduce(BigDecimal::add).get();
        }

        payStep.setStepWins(scatterWins.add(payWayWins).add(payLineWins));
        return payStep.getStepWins();
    }
}
