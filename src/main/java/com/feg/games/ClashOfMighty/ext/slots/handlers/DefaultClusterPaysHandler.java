package com.feg.games.ClashOfMighty.ext.slots.handlers;

import com.feg.games.ClashOfMighty.ext.slots.bonus.handler.BonusMultiplierHandler;
import com.feg.games.ClashOfMighty.ext.slots.model.Cluster;
import com.feg.games.ClashOfMighty.ext.slots.reels.SymbolGrid;
import com.feg.games.ClashOfMighty.ext.api.symbol.Symbol;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

import java.math.BigDecimal;
import java.util.*;


@Component
public class DefaultClusterPaysHandler<TSymbol extends Symbol> {


    public List<Cluster> findClusters(SymbolGrid symbolGrid,
                                      List<Integer> expandingReels,
                                      List<TSymbol> replacingSymbols,
                                      List<TSymbol> nonReplaceableSymbols,
                                      LinkedMultiValueMap<? extends Symbol, BigDecimal> symbolStakeMultipliers,
                                      BonusMultiplierHandler bonusMultiplierHandler
                                      ) {

        List<Cluster> clusters = new LinkedList<>();


        int noOfRows = symbolGrid.getRows();
        int noOfReels = symbolGrid.getColumns();
        LinkedMultiValueMap<Integer, Symbol> symbols = symbolGrid.getSymbolGrid();

        BigDecimal bonusMultiplier = BigDecimal.ONE;
        if(bonusMultiplierHandler!=null)
            bonusMultiplier = bonusMultiplierHandler.getBonusMultiplier();

        boolean[][] visited = new boolean[noOfRows][noOfReels];
        Map<String, Boolean> symbolWildContribution = new HashMap<>();

        for (int i = 0; i < noOfReels; i++){
            for (int j = 0; j < noOfRows; j++) {
                TSymbol symbol = (TSymbol) symbols.get(i).get(j);
                if (visited[j][i] || replacingSymbols.stream().anyMatch(tSymbol -> tSymbol == symbol)) continue;

                List<Integer> clusterPositions = new ArrayList<>();
                if(!expandingReels.isEmpty()) {
                    expandingReels.stream().forEach(index -> {
                        for (int x = 0; x < visited.length; x++) {
                            visited[x][index] = false;
                        }
                    });
                }

                int count = dfs(noOfReels, noOfRows, i, j, symbols, symbol, clusterPositions, visited, symbolWildContribution, replacingSymbols);

                if(count>0){
                    BigDecimal multiplier;
                    int size = symbolStakeMultipliers.get(symbol).size();
                    if(count < size )
                        multiplier =  symbolStakeMultipliers.get(symbol).get(count - 1);
                    else
                        multiplier = symbolStakeMultipliers.get(symbol).get(size - 1);
                    if(multiplier.doubleValue()>0){
                        clusters.add(new Cluster(symbol, multiplier , bonusMultiplier, clusterPositions , count));
                    }
                }
            }
        }

        return clusters;
    }

    public int dfs(int noOfReels,
                   int noOfRows,
                   int reel, int row,
                   LinkedMultiValueMap<Integer, Symbol> symbols, TSymbol symbol,
                   List<Integer> clusterPositions, boolean[][] visited, Map<String, Boolean> symbolWildContribution, List<TSymbol> replacingSymbols) {
        //if (i < 0 || j < 0 || i >= this.noOfRows || j >= this.noOfReels || visited[i][j]  || (!grid[i][j].equals(symbol) && !grid[i][j].equals(R4BBSymbol.WILD))) return 0;

        //NEW
        if (reel < 0 || row < 0  || row>=noOfRows || reel>=noOfReels)
            return 0;
        boolean wildContribution = replacingSymbols.stream().anyMatch(tSymbol -> symbols.get(reel).get(row).equals(tSymbol));
        if((!symbols.get(reel).get(row).equals(symbol) && !wildContribution))
            return 0;
        if(visited[row][reel]
           && (!wildContribution || (symbolWildContribution.get(symbol.getCode()+""+reel+""+row) != null
                                     && symbolWildContribution.get(symbol.getCode()+""+reel+""+row))))
            return 0;

        //OLD
        /*if (reel < 0 || row < 0  || row>=noOfRows || reel>=noOfReels || visited[row][reel] || (!symbols.get(reel).get(row).equals(symbol)
                                                                                               && replacingSymbols.stream().noneMatch(tSymbol -> symbols.get(reel).get(row)==tSymbol)))
            return 0;*/

        int count = 1;
        visited[row][reel] = true;
        if(symbolWildContribution.get(symbol.getCode()+""+reel+""+row)==null
           || !symbolWildContribution.get(symbol.getCode()+""+reel+""+row))
            clusterPositions.add(reel * noOfReels + row);

        if(wildContribution)
            symbolWildContribution.put(symbol.getCode()+""+reel+""+row, true);

        count += dfs(noOfReels, noOfRows, reel + 1, row,symbols, symbol, clusterPositions, visited,symbolWildContribution, replacingSymbols);
        count += dfs(noOfReels, noOfRows,reel, row + 1,symbols, symbol, clusterPositions, visited,symbolWildContribution, replacingSymbols);
        count += dfs(noOfReels, noOfRows, reel - 1 ,row,symbols, symbol, clusterPositions, visited,symbolWildContribution, replacingSymbols);
        count += dfs(noOfReels, noOfRows,reel,row - 1,symbols, symbol, clusterPositions, visited,symbolWildContribution, replacingSymbols);
        return count;
    }
}
