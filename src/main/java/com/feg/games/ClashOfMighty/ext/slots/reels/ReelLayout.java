package com.feg.games.ClashOfMighty.ext.slots.reels;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.feg.games.ClashOfMighty.ext.slots.utils.Weighted;
import com.feg.games.ClashOfMighty.ext.api.bonus.GameBonus;
import com.feg.games.ClashOfMighty.ext.api.symbol.Symbol;
import lombok.Data;

import java.util.List;

/**
 * The ReelLayout.
 */
@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReelLayout<TSymbol extends Symbol, TGameBonus extends GameBonus>
        implements Weighted {

    public Integer index;
    public TGameBonus baseGame;
    public TGameBonus bonusGame;
    public Integer weight;
    public List<SlotReel<TSymbol>> reels;
    String name;
}


