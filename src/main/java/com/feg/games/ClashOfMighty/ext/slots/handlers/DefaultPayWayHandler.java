package com.feg.games.ClashOfMighty.ext.slots.handlers;

import com.feg.games.ClashOfMighty.ext.slots.model.PayWay;
import com.feg.games.ClashOfMighty.ext.slots.reels.SymbolGrid;
import com.feg.games.ClashOfMighty.ext.api.symbol.Symbol;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


@Component
public class DefaultPayWayHandler {

    public List<PayWay> findPayWays(SymbolGrid symbolGrid,
                                    //SlotCart<TSymbol> slotCart,
                                    List<? extends Symbol> replacingSymbols,
                                    List<? extends Symbol> nonReplaceableSymbols,
                                    LinkedMultiValueMap<? extends Symbol, BigDecimal> symbolStakeMultipliers,
                                    BigDecimal bonusMultiplier) {
        List<PayWay> payWayList = new LinkedList<>();

        Set<Symbol> targetSymbols = symbolGrid.extractDistinctSymbols(0, false);

        for (Symbol symbol : targetSymbols) {
            List<Integer> noOfSymbolsByReel = new ArrayList<>();

            List<List<Integer>> reelMatchingSymbolPositions = new LinkedList<>();
            //LinkedList<Integer> cartMatchingSymbolPositions = new LinkedList<>();
            int cartIndex;
            for (int reel = 0; reel < symbolGrid.getSymbolGrid().size(); reel++) {
                List<Integer> symbolPositions = symbolGrid.getSymbolPositionsOnReel(symbol, reel, replacingSymbols);
                int count = symbolPositions.size();

                /*if (slotCart != null) {
                    cartIndex = reel - 1;
                    if (slotCart.getSymbolPositions(symbol, cartIndex, replacingSymbols)) {
                        count++;
                        cartMatchingSymbolPositions.add(cartIndex);
                    }
                }*/

                if (count == 0)
                    break;

                noOfSymbolsByReel.add(count);
                reelMatchingSymbolPositions.add(symbolPositions);
            }
            @SuppressWarnings("ConstantConditions")
            BigDecimal multiplier = symbolStakeMultipliers.get(symbol).get(noOfSymbolsByReel.size() - 1);
            if (multiplier.floatValue() <= 0)
                continue;

            int payWays = 1;
            for (Integer i : noOfSymbolsByReel)
                payWays = i * payWays;

            PayWay payWay = new PayWay(symbol, multiplier, bonusMultiplier, payWays, noOfSymbolsByReel,
                    reelMatchingSymbolPositions);
            payWayList.add(payWay);
        }

        return payWayList;
    }
}
