package com.feg.games.ClashOfMighty.ext.slots.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.feg.games.ClashOfMighty.ext.slots.reels.ReelLayout;
import com.feg.games.ClashOfMighty.ext.api.bonus.GameBonus;
import com.feg.games.ClashOfMighty.ext.api.exception.GameEngineException;
import com.feg.games.ClashOfMighty.ext.api.symbol.Symbol;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The SlotsReelConfiguration.
 */
@Data
@NoArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class SlotsReelConfiguration<TSymbol extends Symbol, TGameBonus extends GameBonus> {


    private List<ReelLayout<TSymbol, TGameBonus>> reelLayouts;

    private LinkedMultiValueMap<TGameBonus, List<Integer>> reelRowWeights;

    public List<ReelLayout<TSymbol, TGameBonus>> getReelLayouts(GameBonus gameBonus){
        List<ReelLayout<TSymbol, TGameBonus>> rls =
                this.getReelLayouts()
                        .stream()
                        .filter(reelLayout ->
                                reelLayout.getBaseGame().getName()
                                        .equals(gameBonus.getName()))
                        .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(rls)) {
            throw new GameEngineException("No reel layout found for the game play " + gameBonus);
        }
        return rls;
    }

    public List<ReelLayout<TSymbol, TGameBonus>> getReelLayouts(String gameBonus){
        List<ReelLayout<TSymbol, TGameBonus>> rls =
                this.getReelLayouts()
                        .stream()
                        .filter(reelLayout ->
                                reelLayout.getBaseGame().getName()
                                        .equals(gameBonus))
                        .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(rls)) {
            throw new GameEngineException("No reel layout found for the game play " + gameBonus);
        }
        return rls;
    }

}
