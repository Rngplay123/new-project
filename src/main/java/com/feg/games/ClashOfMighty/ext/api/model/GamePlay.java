package com.feg.games.ClashOfMighty.ext.api.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * A Class that captures the state of a entire game. All the game activities against a game are stored in this object.
 */
@Data
@NoArgsConstructor
public class GamePlay implements Serializable {

//    private String uid;
//    private String gameConfiguration;
//    protected Date createdOn = new Date();
//    protected Date modifiedOn = new Date();
//    protected boolean deleted = false;
    private GameType gameType;

    private GamePlayState gamePlayState;
    GameStatus status;

    private static Integer SCALE = 0;
    private static RoundingMode ROUNDING_MODE = null;
    static {
        SCALE = Integer.valueOf(System.getProperty("engine.amounts.scale", "2"));
        ROUNDING_MODE = RoundingMode.valueOf(System.getProperty("engine.amounts.roundingMode", "HALF_UP"));
    }

    public GamePlay(GameType gameType) {
        this.gameType = gameType;
    }

    public static BigDecimal scaledValue( BigDecimal unscaled){
       return unscaled.setScale(SCALE, ROUNDING_MODE);
    }
}
