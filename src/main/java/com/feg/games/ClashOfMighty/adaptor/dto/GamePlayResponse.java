package com.feg.games.ClashOfMighty.adaptor.dto;

import com.feg.games.ClashOfMighty.ext.slots.api.dto.SlotsGameEngineResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GamePlayResponse {

    private BigDecimal win;
    private boolean cashOut;
    private MathState mathState;
    private SlotsGameEngineResponse gameData;

}