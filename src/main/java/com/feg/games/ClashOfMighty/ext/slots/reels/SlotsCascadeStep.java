/*
package com.rngplay.gaming.slots.reels;

import com.rngplay.gaming.ext.model.slots.SlotsSpinGameActivity;
import com.rngplay.gaming.slots.symbol.SlotSymbol;

import com.rngplay.gaming.ext.model.slots.PayWay;
import lombok.Data;

import org.joda.money.Money;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Data
public final class SlotsCascadeStep<TSymbol extends SlotSymbol> {

    private Money winnings;
    private List<PayWay> payWays = new ArrayList<>();
    private List<PayWay> payLines = new ArrayList<>();
    private Map<Integer, TreeSet<Integer>> reactingSymbolPositions = new HashMap<>();
    private TreeSet<Integer> reactingSymbolPositionsInCart = new TreeSet<>();
    private SymbolGrid beforeStepGrid;
    private SlotCart<TSymbol> beforeStepCart;
    private SymbolGrid afterStepGrid;
    private SlotCart<TSymbol> afterStepCart;

    private SlotsCascadeStep(){

    }


    public  SlotsCascadeStep<TSymbol> addCascadeStep(SlotsSpinGameActivity<TSymbol, SlotsBonusContext<TSymbol>> spinResult,
                                                     SymbolGrid symbolGrid,
                                                     SlotCart<TSymbol> slotCart,
                                                     List<PayWay> payWays) throws GameEngineException {

        SlotsCascadeStep<TSymbol> step = new SlotsCascadeStep<>();

        SlotsCascadeStep<TSymbol> last = spinResult.getCascadeSteps().peekLast();

        SymbolGrid beforeStepGrid;
        SlotCart<TSymbol> beforeStepCart;

        if(last!=null){
            beforeStepGrid = new SymbolGrid(last.getAfterStepGrid());
            beforeStepCart = new SlotCart<>(last.getAfterStepCart());
        }else{
            beforeStepGrid = new SymbolGrid(symbolGrid);
            beforeStepCart = new SlotCart<>(slotCart);
        }

        SymbolGrid afterStepGrid = null;
        SlotCart<TSymbol> afterStepCart = null;
        if(!payWays.isEmpty()) {
            afterStepGrid = new SymbolGrid(beforeStepGrid);
            afterStepCart = new SlotCart<>(beforeStepCart);

            for (PayWay payWay : payWays) {
                for (int reel = 0; reel < payWay.getReelSymbolPositions().size(); reel++) {
                    TreeSet<Integer> reactingPositions = new TreeSet<>();
                    if(step.getReactingSymbolPositions().get(reel)!=null){
                        reactingPositions.addAll(step.getReactingSymbolPositions().get(reel));
                    }
                    reactingPositions.addAll(payWay.getReelSymbolPositions().get(reel));
                    step.getReactingSymbolPositions().put(reel, reactingPositions);
                }
                if (payWay.getCartSymbolPositions()!=null) {
                    step.getReactingSymbolPositionsInCart().addAll(payWay.getCartSymbolPositions());
                }
            }

            Set<Map.Entry<Integer, TreeSet<Integer>>> entries = step.getReactingSymbolPositions().entrySet();

            for(Map.Entry<Integer, TreeSet<Integer>> entry: entries){
                afterStepGrid.cascade(entry.getKey(), entry.getValue());
            }

            afterStepCart.cascade(step.getReactingSymbolPositionsInCart());
        }

        step.setBeforeStepGrid(beforeStepGrid);
        step.setBeforeStepCart(beforeStepCart);

        step.setAfterStepGrid(afterStepGrid);
        step.setAfterStepCart(afterStepCart);
        step.setPayWays(payWays);
        spinResult.getCascadeSteps().add(step);
        return step;
    }
}
*/
