package com.feg.games.ClashOfMighty.adaptor.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameRequest {
    private MathState mathState;
    private GameData gameData;
}