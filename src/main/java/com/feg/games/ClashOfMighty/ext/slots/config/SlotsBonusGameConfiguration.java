package com.feg.games.ClashOfMighty.ext.slots.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.feg.games.ClashOfMighty.ext.slots.bonus.BonusOption;
import com.feg.games.ClashOfMighty.ext.api.bonus.GameBonus;
import com.feg.games.ClashOfMighty.ext.api.symbol.Symbol;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * The SlotsBonusGameConfiguration.
 */
@Data
@NoArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class SlotsBonusGameConfiguration<TSymbol extends Symbol, TGameBonus extends GameBonus>
        implements BonusConfiguration<TSymbol, TGameBonus> {

    TSymbol scatterSymbol;
    private Integer[] scatterStakeMultipliers;
    private SlotsFreeGameConfiguration<TGameBonus> freeGameConfiguration;
    private List<BonusOption<TGameBonus>> bonusOptions;
    private HashMap<TGameBonus, LinkedHashMap<TSymbol, Integer>> mysterySymbols;

}