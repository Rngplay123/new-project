package com.feg.games.ClashOfMighty.ext.slots.reels;

import com.feg.games.ClashOfMighty.engine.model.RNG;
import com.feg.games.ClashOfMighty.ext.slots.config.SlotsReelConfiguration;
import com.feg.games.ClashOfMighty.ext.api.bonus.GameBonus;
import com.feg.games.ClashOfMighty.ext.api.symbol.Symbol;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The DefaultSlotReelSpinner.
 */
@Slf4j
@Data
@Component
public class DefaultSlotReelSpinner {

    @Autowired
    RNG rng;
    public <S extends Symbol, G extends GameBonus> List<Integer> reelSpin(SlotsReelConfiguration<S, G> reelConfiguration,
                                                                          List<SlotReel<S>> reelLayout) {

        return getTopReelIndexList(reelLayout, reelLayout.size());
    }

    private <S extends Symbol> List<Integer> getTopReelIndexList(List<SlotReel<S>> reelLayout, int reels) {
        return IntStream.range(0, reels)
                .map(reel -> getTopReelIndex(reelLayout, reel))
                .boxed()
                .collect(Collectors.toList());
    }

    private <S extends Symbol> Integer getTopReelIndex(List<SlotReel<S>> reelLayout, Integer reel) {
        return rng.nextInt(reelLayout
                .get(reel)
                .getSymbols()
                .size());
    }
}
