package com.feg.games.ClashOfMighty.adaptor.dto;

import com.feg.games.ClashOfMighty.ext.api.model.GamePlay;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MathState {
    private Map<String, Object> persistentData;
    private GamePlay gamePlayData;
}
