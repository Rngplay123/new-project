package com.feg.games.ClashOfMighty.ext.api.symbol;

import java.io.Serializable;
import java.util.List;

/**
 * An Interface for Game Symbols.
 */
public interface Symbol extends Serializable {

    /**
     * @return A list of {@link SymbolType}'s
     */
    List<SymbolType> getSymbolType();

    /**
     * @return Symbol code.
     */
    String getCode();

}
