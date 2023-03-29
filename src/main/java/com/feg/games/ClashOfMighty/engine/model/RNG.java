package com.feg.games.ClashOfMighty.engine.model;

import com.feg.games.ClashOfMighty.engine.bonus.RngGameBonus;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
@Component
public class RNG {

    private final Random random = new Random();

    public int nextInt(int size) {
       // random.setSeed(fetch from link);
        return this.random.nextInt(size);
    }

    public Map<String,Integer> randomProvider(HashMap<String, Integer> randomList){
        Map<String,Integer> randomNumbersWithKey = new HashMap<>();
        for (String name : randomList.keySet())
        {
            randomNumbersWithKey.put(name,nextInt(randomList.get(name)));
        }
        return randomNumbersWithKey;
    }


}
