package com.feg.games.ClashOfMighty.ext.slots.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.feg.games.ClashOfMighty.ext.slots.model.SymbolLine;
import com.feg.games.ClashOfMighty.ext.api.model.GameConfiguration;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The SlotsBaseConfiguration.
 */
@Data
@NoArgsConstructor

public class SlotsBaseConfiguration extends GameConfiguration {

    public String name;

    public int rows;

    public int columns;

    public int[][] payLineOffsets;

    public SymbolLine.LineDirection lineDirection;
}
