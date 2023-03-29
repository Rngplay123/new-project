package com.feg.games.ClashOfMighty.ext.slots.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.feg.games.ClashOfMighty.ext.api.bonus.GameBonus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

/**
 * The SlotsFreeGameConfiguration.
 */
@Data
@NoArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)

public class SlotsFreeGameConfiguration<TGameBonus extends GameBonus> {

    Map<Integer, Integer> triggerCount;

    Map<Integer, Integer> triggerWeights;

    TGameBonus awardGameBonus;

    BigDecimal multiplier;
}
