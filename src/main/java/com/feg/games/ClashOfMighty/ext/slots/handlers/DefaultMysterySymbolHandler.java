package com.feg.games.ClashOfMighty.ext.slots.handlers;

import com.feg.games.ClashOfMighty.ext.slots.model.SlotsSpinGameActivity;
import com.feg.games.ClashOfMighty.ext.slots.utils.PickByWeightage;
import com.feg.games.ClashOfMighty.ext.slots.utils.Weighted;
import com.feg.games.ClashOfMighty.ext.api.bonus.GameBonus;
import com.feg.games.ClashOfMighty.ext.api.exception.GameEngineException;
import com.feg.games.ClashOfMighty.ext.api.symbol.Symbol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;


@Component
public class DefaultMysterySymbolHandler<TSymbol extends Symbol & Weighted, TGameBonus extends GameBonus> {

    @Autowired
    PickByWeightage pickByWeightage;

    public void replaceMysterySymbols(SlotsSpinGameActivity slotsSpinGameActivity,
                          TGameBonus bonusAwarded,
                          HashMap<TGameBonus, LinkedHashMap<TSymbol,Integer>> mysterySymbolsWeights,
                          Symbol mysterySymbol) throws GameEngineException {


        if (mysterySymbolsWeights == null || mysterySymbolsWeights.get(bonusAwarded) == null)
            return;


        LinkedHashMap<TSymbol, Integer> symbolWeights = mysterySymbolsWeights.get(bonusAwarded);

        List<TSymbol> symbols = new LinkedList<>();
        symbolWeights.forEach((symbol, weight) -> {
            symbol.setWeight(weight);
            symbols.add(symbol);
        });

        TSymbol symbolPicked = pickByWeightage.pick(symbols);

        if(slotsSpinGameActivity.getSpecialSymbols()==null)
            slotsSpinGameActivity.setSpecialSymbols(new HashMap<>());

        slotsSpinGameActivity.getSpecialSymbols().put(bonusAwarded, symbolPicked);

        slotsSpinGameActivity
                .getLastPayStep()
                .getStepSymbolGrid().replaceSymbolsOnGrid(mysterySymbol, symbolPicked, false);
    }
}
