package com.feg.games.ClashOfMighty.ext.slots.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.feg.games.ClashOfMighty.ext.api.bonus.GameBonus;
import com.feg.games.ClashOfMighty.ext.api.model.GamePlayState;
import com.feg.games.ClashOfMighty.ext.api.model.GameStatus;
import com.feg.games.ClashOfMighty.ext.api.model.GameType;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class SlotsGamePlayState extends GamePlayState {

    private BigDecimal totalStake;
    private BigDecimal stakePerLine;
    private BigDecimal totalWinnings = BigDecimal.ZERO;
    private List<GameBonus> bonusAwarded = new LinkedList<>();
    private int noOfLines;

    public SlotsGamePlayState(GameStatus gameStatus) {
        super(gameStatus, GameType.SLOTS);
    }

    public SlotsGamePlayState(GameStatus gameStatus, int playingLines) {
        super(gameStatus, GameType.SLOTS);
        //this.noOfLines = playingLines;
    }

    @JsonIgnore
    public GameBonus getLastBonusAwarded() {
        return this.bonusAwarded.isEmpty() ? null : this.bonusAwarded.get(this.bonusAwarded.size() - 1);
    }

    @JsonIgnore
    public GameBonus getFirstBonusAwarded() {
        return this.getBonusAwarded().get(1);
    }

}
