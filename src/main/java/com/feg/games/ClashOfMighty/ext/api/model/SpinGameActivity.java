package com.feg.games.ClashOfMighty.ext.api.model;

import com.feg.games.ClashOfMighty.ext.api.bonus.BonusContext;
import lombok.Data;

import java.util.List;

/**
 * A Generic GameActivity class to capture a Wheel Spin. (In this case spins positions should only contain one positions)
 * Or can be used to capture top reel symbol positions of Slots Game.
 */
@Data
public class SpinGameActivity extends GameActivity {

    List<Integer> spinPositions;

    protected SpinGameActivity() {
        super();
    }

    public SpinGameActivity(GameStatus gameStatus, List<Integer> spinPositions, BonusContext bonusContext) {
        super(gameStatus, bonusContext);
        this.spinPositions = spinPositions;
    }

}
