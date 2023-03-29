package com.feg.games.ClashOfMighty.ext.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.feg.games.ClashOfMighty.ext.api.bonus.BonusContext;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Game Activity Object that captures the state of a game play.
 */

@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class GameActivity  implements Serializable {

    private String uid;
    protected String tenant;
    protected String account;
    private GameStatus gameStatus;
    private BonusContext bonusContext;
    protected Date createdOn = new Date();
    protected Date modifiedOn = new Date();
    private BigDecimal wager;
    private BigDecimal winnings;
    private BigDecimal totalWinnings;
    protected boolean deleted = false;

    public GameActivity() {
        super();
    }

    public GameActivity(GameStatus gameStatus, BonusContext bonusContext) {
        super();
        this.gameStatus = gameStatus;
        this.createdOn = new Date();
        this.bonusContext = bonusContext;
    }

    public GameActivity(GameStatus gameStatus, GamePlay gamePlay, BonusContext bonusContext) {
        this(gameStatus, bonusContext);
    }
}
