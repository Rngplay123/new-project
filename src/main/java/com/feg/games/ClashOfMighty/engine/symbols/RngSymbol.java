package com.feg.games.ClashOfMighty.engine.symbols;

import com.feg.games.ClashOfMighty.ext.api.symbol.Symbol;
import com.feg.games.ClashOfMighty.ext.api.symbol.SymbolType;
import com.feg.games.ClashOfMighty.ext.slots.utils.Weighted;

import java.util.Arrays;
import java.util.List;

public enum RngSymbol implements Symbol, Weighted {
    WD("WD"       ,    SymbolType.WILD    ,  SymbolType.SPECIAL ),
    SC("SC"       ,    SymbolType.SCATTER ,  SymbolType.SPECIAL ),
    TH("TH"       ,    SymbolType.NORMAL                        ),
    LO("LO"       ,    SymbolType.NORMAL                        ),
    H2("H2"       ,    SymbolType.NORMAL                        ),
    H3("H3"       ,    SymbolType.NORMAL                        ),
    H4("H4"       ,    SymbolType.NORMAL                        ),
    H5("H5"       ,    SymbolType.NORMAL                        ),
    L1("L1"       ,    SymbolType.NORMAL                        ),
    L2("L2"       ,    SymbolType.NORMAL                        ),
    L3("L3"       ,    SymbolType.NORMAL                        ),
    L4("L4"       ,    SymbolType.NORMAL                        ),
    L5("L5"       ,    SymbolType.NORMAL                        ),
    L6("L6"       ,    SymbolType.NORMAL                        ),
    Blank("Blank"       ,    SymbolType.NORMAL                        );

    private final String code;

    private List<SymbolType> symbolType;

    private int weight;

    RngSymbol(String code, SymbolType... type) {
        this.code = code;
        this.symbolType = Arrays.asList(type);
    }

    RngSymbol(String code, int weight, SymbolType... type) {
        this.code = code;
        this.symbolType = Arrays.asList(type);
        this.weight = weight;
    }

    RngSymbol(String code, int weight) {
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
