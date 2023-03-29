package com.feg.games.ClashOfMighty.adaptor.dto;
import com.feg.games.ClashOfMighty.engine.config.RngGameConfiguration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor

public class GameInitResponse {
    private MathState mathState;
    private RngGameConfiguration gameData;
}