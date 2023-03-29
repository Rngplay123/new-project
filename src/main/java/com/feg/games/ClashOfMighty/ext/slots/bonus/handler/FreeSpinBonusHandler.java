package com.feg.games.ClashOfMighty.ext.slots.bonus.handler;

import com.feg.games.ClashOfMighty.ext.slots.config.SlotsFreeGameConfiguration;
import com.feg.games.ClashOfMighty.ext.slots.model.SlotsBonusContext;
import com.feg.games.ClashOfMighty.ext.slots.utils.PickByWeightage;
import com.feg.games.ClashOfMighty.ext.slots.bonus.BonusMultiplier;
import com.feg.games.ClashOfMighty.ext.slots.bonus.FreeSpinsContext;
import com.feg.games.ClashOfMighty.ext.slots.bonus.BonusOption;
import com.feg.games.ClashOfMighty.ext.api.bonus.GameBonus;
import com.feg.games.ClashOfMighty.ext.api.exception.GameEngineException;
import com.feg.games.ClashOfMighty.ext.api.symbol.Symbol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Component("freeSpinBonusHandler")
public class FreeSpinBonusHandler<TSymbol extends Symbol, TGameBonus extends GameBonus> {

    @Autowired
    PickByWeightage pickByWeightage;

    public TGameBonus awardBonus(SlotsBonusContext slotsBonusContext,
                                 int scatterCount,
                                 SlotsFreeGameConfiguration<TGameBonus> fgc) {


        Optional<Integer> freeSpins = fgc.getTriggerCount()
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().equals(scatterCount))
                .map(entry -> entry.getValue() != null ? entry.getValue() : 0)
                .findAny();

        if(freeSpins.isEmpty() && fgc.getTriggerWeights()!=null){
            freeSpins = Optional.of(pickByWeightage.pickFromMapWithValuesAsWeights(fgc.getTriggerWeights()));
        }

        if (freeSpins.isPresent()) {
            if (freeSpins.get() > 0) {
                FreeSpinsContext freeSpinsContext = slotsBonusContext.getFreeSpinsContext();
                freeSpinsContext = FreeSpinsContext.awardFreeSpins(freeSpinsContext, freeSpins.get());
                slotsBonusContext.setFreeSpinsContext(freeSpinsContext);
                return fgc.getAwardGameBonus();

            }
            return fgc.getAwardGameBonus();
        }


        return null;
    }

    public FreeSpinsContext consumeBonus(FreeSpinsContext freeSpinsContext) throws GameEngineException {
        if (freeSpinsContext == null)
            return null;

        freeSpinsContext.consumeFreeSpin();

        return freeSpinsContext;
    }


    public void  awardFreeSpins(Integer freeSpins, List<BonusOption<TGameBonus>> bonusOptions, SlotsBonusContext bonusContext) throws GameEngineException {

        FreeSpinsContext freeSpinsContext = bonusContext.getFreeSpinsContext();
        BonusOption<TGameBonus> picked = null;
        if (freeSpins!=null){
            if(!CollectionUtils.isEmpty(bonusOptions)){
                Integer finalFreeSpins = freeSpins;
                picked = bonusOptions.stream().filter(option -> option.getBonusValue().equals(finalFreeSpins))
                        .findAny()
                        .orElseGet(null);
            }
        }else if(!CollectionUtils.isEmpty(bonusOptions)){
            picked = pickByWeightage.pick(bonusOptions);
            freeSpins = picked.getBonusValue();
        }

        if(freeSpins!=null){
            freeSpinsContext = FreeSpinsContext.awardFreeSpins(freeSpinsContext, freeSpins);
            bonusContext.setFreeSpinsContext(freeSpinsContext);
            if(picked!=null){
                if(!CollectionUtils.isEmpty(picked.getBonusMultipliers())){
                    BonusMultiplier bonusMultiplier = pickByWeightage.pick(picked.getBonusMultipliers());
                    bonusContext.setBonusMultiplier(bonusMultiplier.getMultiplier());
                }else{
                    bonusContext.setBonusMultiplier(BigDecimal.ONE);
                }
                bonusContext.setBonusOptionAwarded(picked);
            }
        }
    }

}
