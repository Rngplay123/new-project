package com.feg.games.ClashOfMighty.ext.api.model;

import com.feg.games.ClashOfMighty.ext.api.bonus.BonusContext;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public abstract class GamePlayState implements Serializable {

    @NonNull
    GameStatus gameStatus;

    @NonNull
    GameType gameType;

    BonusContext bonusContext;


    Map<String, Object> data = new HashMap<>();
}
