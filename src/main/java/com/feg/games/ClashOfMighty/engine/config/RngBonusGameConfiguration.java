package com.feg.games.ClashOfMighty.engine.config;

import com.feg.games.ClashOfMighty.engine.symbols.RngSymbol;
import com.feg.games.ClashOfMighty.engine.bonus.RngGameBonus;
import com.feg.games.ClashOfMighty.ext.slots.config.SlotsBonusGameConfiguration;
import com.feg.games.ClashOfMighty.engine.model.WheelOption;
import lombok.Data;

import java.util.List;

/**
 * The ROAGameConfiguration.
 */
@Data
public class RngBonusGameConfiguration extends SlotsBonusGameConfiguration<RngSymbol, RngGameBonus> {

 List<WheelOption> wheelOptions;
}
