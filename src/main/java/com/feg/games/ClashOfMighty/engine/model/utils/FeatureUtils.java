package com.feg.games.ClashOfMighty.engine.model.utils;


import com.feg.games.ClashOfMighty.ext.api.symbol.Symbol;
import org.springframework.util.LinkedMultiValueMap;

import java.util.ArrayList;
import java.util.List;

public class FeatureUtils {
    public static LinkedMultiValueMap<Integer, String> convertSymbolGridToStringGrid(LinkedMultiValueMap<Integer, Symbol> symbolGrid) {
        LinkedMultiValueMap<Integer, String> stringSymbolGrid = new LinkedMultiValueMap<>();
        for(int i= 0; i < symbolGrid.size(); i++) {
            List<String> symList = new ArrayList<>();
            for(int k=0; k < symbolGrid.get(i).size(); k++) {
                symList.add( symbolGrid.get(i).get(k).toString() );
            }
            stringSymbolGrid.put(i, symList);
        }
        return stringSymbolGrid;
    }
    public static LinkedMultiValueMap<Integer, String> createStackingSymbolDisplayGrid(LinkedMultiValueMap<Integer, Symbol> symbolGrid, List<String> stackSettings, int reels, int display) {
        LinkedMultiValueMap<Integer, String> featureGrid = convertSymbolGridToStringGrid(symbolGrid);
        return featureGrid;
    }

    public static LinkedMultiValueMap<Integer, Symbol> reverseSymbolGrid(LinkedMultiValueMap<Integer, Symbol> stepSymbolGrid) {
        LinkedMultiValueMap<Integer, Symbol> tempSymbolGrid = new LinkedMultiValueMap<>();
        for(int i=stepSymbolGrid.size();i>0;i--){
            tempSymbolGrid.put(stepSymbolGrid.size()-i,stepSymbolGrid.get(i-1));
        }
        return tempSymbolGrid;
    }


}
