package com.feg.games.ClashOfMighty.adaptor.dto;

import com.feg.games.ClashOfMighty.engine.model.dto.RngGameEngineRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BetRequest {

    private BigDecimal bet;
    private MathState mathState;
    private RngGameEngineRequest gameData;

}
