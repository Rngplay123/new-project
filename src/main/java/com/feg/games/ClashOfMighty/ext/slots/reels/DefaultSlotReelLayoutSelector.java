package com.feg.games.ClashOfMighty.ext.slots.reels;

import com.feg.games.ClashOfMighty.ext.slots.config.SlotsReelConfiguration;
import com.feg.games.ClashOfMighty.ext.slots.utils.PickByWeightage;
import com.feg.games.ClashOfMighty.ext.api.bonus.GameBonus;
import com.feg.games.ClashOfMighty.ext.api.exception.GameEngineException;
import com.feg.games.ClashOfMighty.ext.api.symbol.Symbol;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The DefaultSlotReelLayoutSelector.
 */
@Slf4j
@Data
@Component
public class DefaultSlotReelLayoutSelector {

    @Autowired
    PickByWeightage pickByWeightage;

    public <S extends Symbol, T extends GameBonus> ReelLayout<S, T> selectReelLayout(T playingBonus, SlotsReelConfiguration<S, T> rc) throws GameEngineException {
        List<ReelLayout<S, T>> reelLayouts = rc.getReelLayouts(playingBonus);

        if (reelLayouts.size() > 1)
            return pickByWeightage.pick(reelLayouts);
        else
            return reelLayouts.get(0);
    }

    public <S extends Symbol, T extends GameBonus> ReelLayout<S, T> selectReelLayout(T playingBonus, SlotsReelConfiguration<S, T> rc,int pick) throws GameEngineException {
        List<ReelLayout<S, T>> reelLayouts = rc.getReelLayouts(playingBonus);

        if (reelLayouts.size() > 1)
            return pickByWeightage.pick(reelLayouts,pick);
        else
            return reelLayouts.get(0);
    }

}
