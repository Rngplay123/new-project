package com.feg.games.ClashOfMighty.adaptor.dto;


import com.feg.games.ClashOfMighty.engine.config.RngGameConfiguration;
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
public class GameData {

    private String gd;
    Map<String, RngGameConfiguration> configurationMap= new HashMap<>();

}
