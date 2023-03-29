package com.feg.games.ClashOfMighty.engine.bonus;

import com.feg.games.ClashOfMighty.engine.model.WheelOption;
import com.feg.games.ClashOfMighty.ext.api.model.GameStatus;
import com.feg.games.ClashOfMighty.ext.slots.model.SlotsBonusContext;
import lombok.Data;

import java.util.List;

@Data
public class RngBonusContext extends SlotsBonusContext {
    List<WheelOption>   wheelOptions;
    WheelOption         wheelStop;
    int                 scatterCount;
    List<List<Integer>> specialSymbolPosition;
    GameStatus gameStatus;
    public RngBonusContext()
    {
        super( );
    }

    public RngBonusContext(RngBonusContext bonusContext) {
        super(bonusContext);
    }
}
