package com.feg.games.ClashOfMighty.ext.slots.reels;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.feg.games.ClashOfMighty.ext.api.symbol.Symbol;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The SlotReel.
 */
@Data
@NoArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class SlotReel<TSymbol extends Symbol> {
    CascadeDirection cascade;
    private List<TSymbol> symbols;
    private boolean dynamic;
}
