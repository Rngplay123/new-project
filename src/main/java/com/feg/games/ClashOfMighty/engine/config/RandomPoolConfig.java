package com.feg.games.ClashOfMighty.engine.config;

import lombok.Data;

import java.util.HashMap;

@Data
public class RandomPoolConfig {

     public RandomPoolConfig() {
     }

     private int numbers;
     private HashMap<String,Integer> randomList;


}
