package com.feg.games.ClashOfMighty.ext.slots.pays;

import com.feg.games.ClashOfMighty.ext.slots.model.Cluster;
import com.feg.games.ClashOfMighty.ext.slots.model.PayLine;
import com.feg.games.ClashOfMighty.ext.slots.model.PayWay;
import com.feg.games.ClashOfMighty.ext.slots.reels.SymbolGrid;
import com.feg.games.ClashOfMighty.ext.api.symbol.Symbol;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;

import java.util.*;

@NoArgsConstructor
@Data
public class CascadePayStep extends PayStep {

    LinkedMultiValueMap<Integer, Integer> reactingSymbolGrid;

    public CascadePayStep(SymbolGrid symbolGrid, List<Symbol> skipSymbols) {
        super(symbolGrid);
    }

    public CascadePayStep(SymbolGrid symbolGrid, List<Cluster> clusters, List<Symbol> skipSymbols) {
        super(symbolGrid);
        setClusters(clusters);


        Set<Integer> clusterPositions = new HashSet<>();

        clusters.forEach(cluster -> clusterPositions.addAll(cluster.getClusterPositions()));


        this.reactingSymbolGrid = getReactingSymbolRowPositionsOnReels(clusterPositions, skipSymbols);
    }

    public CascadePayStep(SymbolGrid symbolGrid, List<PayLine> payLines, List<PayWay> payWays, List<Symbol> skipSymbols) {
        super(symbolGrid);

        if(!CollectionUtils.isEmpty(payLines))
            this.setPayLines(payLines);
        if(!CollectionUtils.isEmpty(payWays))
            this.setPayWays(payWays);

        reactingSymbolGrid = getReactingSymbolRowPositionsOnReels(skipSymbols);
    }

    private LinkedMultiValueMap<Integer, Integer> getReactingSymbolRowPositionsOnReels(Set<Integer> clusterPositions,  List<Symbol> skipSymbols) {
        LinkedMultiValueMap<Integer, Integer> reactingSymbols = new LinkedMultiValueMap<>();
        int columns = this.getStepSymbolGrid().getColumns();
        //int noOfRows = this.getStepSymbolGrid().getRows();

        clusterPositions.forEach(pos -> {
            int i = pos / columns;
            int j = pos % columns;

            if(!skipSymbols.contains(this.getStepSymbolGrid().getSymbolGrid().get(i).get(j)))
                reactingSymbols.add(i, j);
        });
        return reactingSymbols;
    }

    private LinkedMultiValueMap<Integer, Integer> getReactingSymbolRowPositionsOnReels(List<Symbol> skipSymbols) {
        LinkedMultiValueMap<Integer, Integer> reactingSymbols = new LinkedMultiValueMap<>();
        if(!CollectionUtils.isEmpty(getPayLines())){
            for (PayLine payLine : getPayLines()) {

                for (int reel = 0; reel < payLine.getSymbolCount(); reel++) {
                    int offset = payLine.getPayLineOffSet()[reel];

                    if(skipSymbols.contains(this.getStepSymbolGrid().getSymbolGrid().get(reel).get(offset)))
                        continue;

                    //put only if absent
                    if (reactingSymbols.isEmpty() || (reactingSymbols.get(reel) == null
                                                      || !Objects.requireNonNull(reactingSymbols.get(reel)).contains(offset)) )
                        reactingSymbols.add(reel, offset);
                }
            }
        }else if(!CollectionUtils.isEmpty(getPayWays())){
            for (PayWay payWay : getPayWays()) {
                for (int reel = 0; reel < payWay.getReelSymbolPositions().size(); reel++) {
                    for (Integer reactingSymbolIndex :
                            payWay.getReelSymbolPositions().get(reel)) {

                        if(skipSymbols.contains(this.getStepSymbolGrid().getSymbolGrid().get(reel).get(reactingSymbolIndex)))
                            continue;

                        //put only if absent
                        if (reactingSymbols.isEmpty() || reactingSymbols.get(reel) == null
                            || !Objects.requireNonNull(reactingSymbols.get(reel)).contains(reactingSymbolIndex))
                            reactingSymbols.add(reel, reactingSymbolIndex);
                    }
                }
            }
        }
        return reactingSymbols;
    }
}
