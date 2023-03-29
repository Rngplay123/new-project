package com.feg.games.ClashOfMighty.ext.slots.bonus.handler;

import com.feg.games.ClashOfMighty.ext.slots.model.SlotsSpinGameActivity;
import com.feg.games.ClashOfMighty.ext.slots.reels.SymbolGrid;
import com.feg.games.ClashOfMighty.ext.slots.utils.PickByWeightage;
import com.feg.games.ClashOfMighty.ext.slots.utils.Weighted;
import com.feg.games.ClashOfMighty.ext.slots.api.bonus.types.FreeSpinsBonus;
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
public class ExpandingSymbolPayLineBonusHandler {

    @Autowired
    PickByWeightage pickByWeightage;

    public <TSymbol extends Symbol & Weighted, TGameBonus extends GameBonus> TSymbol awardBonus(SlotsSpinGameActivity slotsSpinGameActivity,
                                                                                                TGameBonus bonusAwarded,
                                                                                                HashMap<TGameBonus, LinkedHashMap<TSymbol, Integer>> expandingSymbols)
            throws GameEngineException {

        if (expandingSymbols == null || expandingSymbols.get(bonusAwarded) == null)
            throw new GameEngineException("no expanding symbols found in game configuration.");


        LinkedHashMap<TSymbol, Integer> symbolWeights = expandingSymbols.get(bonusAwarded);

        List<TSymbol> symbols = new LinkedList<>();
        symbolWeights.forEach((symbol, weight) -> {
            symbol.setWeight(weight);
            symbols.add(symbol);
        });

        TSymbol expandingSymbol = pickByWeightage.pick(symbols);
        if(slotsSpinGameActivity.getSpecialSymbols()==null)
            slotsSpinGameActivity.setSpecialSymbols(new HashMap<>());

        slotsSpinGameActivity.getSpecialSymbols().put(bonusAwarded, expandingSymbol);
        return expandingSymbol;
    }


    public <TGameBonus extends GameBonus>
    SymbolGrid consumeBonus(TGameBonus playingBonus, SymbolGrid symbolGrid, Symbol expandingSymbol)
            throws GameEngineException {

        if (!playingBonus.getName().equals(FreeSpinsBonus.createInstance().getName()))
            return null;

        return symbolGrid.expandSymbolsOnGrid(expandingSymbol);
    }
}
