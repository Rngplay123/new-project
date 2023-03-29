package com.feg.games.ClashOfMighty.engine.config;

import com.feg.games.ClashOfMighty.engine.model.*;
import com.feg.games.ClashOfMighty.engine.symbols.RngSymbol;
import com.feg.games.ClashOfMighty.engine.bonus.RngGameBonus;
import com.feg.games.ClashOfMighty.ext.slots.config.SlotsBaseConfiguration;
import com.feg.games.ClashOfMighty.ext.slots.config.SlotsReelConfiguration;
import lombok.Data;
import org.springframework.util.LinkedMultiValueMap;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * The ROAGameConfiguration.
 */
@Data
public class RngGameConfiguration extends SlotsBaseConfiguration {
    
    SlotsReelConfiguration<RngSymbol, RngGameBonus> reelLayoutConfiguration;
    
    private LinkedMultiValueMap<RngSymbol, BigDecimal> symbolStakeMultipliers;
    
    private RngBonusGameConfiguration bonusGameConfiguration;

    RtpInfo rtpInfo;

    BigDecimal buyFeatureBetMultiplier;

    List<FreeReSpinSymbolOption> freeReSpinSymbolOption;

    List<FreeSpinSymbolOption> freeSpinSymbolOption;

    List<BuyFeatureReelConfiguration> buyFeatureReelConfiguration;

    List<BuyFeatureReels> buyFeatureReels;

    LinkedHashMap<RngGameBonus, RandomPoolConfig> randomConfiguration;

}
