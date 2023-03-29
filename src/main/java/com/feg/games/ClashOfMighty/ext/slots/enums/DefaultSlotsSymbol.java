package com.feg.games.ClashOfMighty.ext.slots.enums;

import com.feg.games.ClashOfMighty.ext.slots.utils.Weighted;
import com.feg.games.ClashOfMighty.ext.api.symbol.Symbol;
import com.feg.games.ClashOfMighty.ext.api.symbol.SymbolType;

import java.util.Arrays;
import java.util.List;

/**
 * The SlotSymbol.
 */
//@JsonTypeName("default")
public enum DefaultSlotsSymbol implements Weighted, Symbol {

    WC("WILD", SymbolType.WILD),
    AA("HIGH_1", SymbolType.NORMAL),
    BB("HIGH_2", SymbolType.NORMAL),
    CC("MID_1", SymbolType.NORMAL),
    DD("MID_2", SymbolType.NORMAL),
    EE("LOW_1", SymbolType.NORMAL),
    FF("LOW_2", SymbolType.NORMAL),
    GG("LOW_3", SymbolType.NORMAL),
    HH("LOW_4", SymbolType.NORMAL),
    JJ("LOW_5", SymbolType.NORMAL);


    private final String code;

    private List<SymbolType> symbolType;

    private int weight;


    DefaultSlotsSymbol(String code, SymbolType... type) {
        this.code = code;
        this.symbolType = Arrays.asList(type);
    }

    DefaultSlotsSymbol(String code, int weight, SymbolType... type) {
        this.code = code;
        this.symbolType = Arrays.asList(type);
        this.weight = weight;
    }

    DefaultSlotsSymbol(String code, int weight) {
        this.code = code;
        this.weight = weight;
    }

    @Override
    public List<SymbolType> getSymbolType() {
        return symbolType;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public Integer getWeight() {
        return weight;
    }

    @Override
    public void setWeight(Integer weight) {
        this.weight = weight;
    }

}
