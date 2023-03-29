package com.feg.games.ClashOfMighty.ext.slots.reels;

import com.feg.games.ClashOfMighty.ext.slots.utils.PickByWeightage;
import com.feg.games.ClashOfMighty.ext.api.symbol.Symbol;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * The SlotOneReel.
 */
@Data
public class SlotDynamicReel<TSymbol extends Symbol> extends SlotReel<TSymbol>  {

    static List<Integer> getReelRowsByWeights(List<List<Integer>> reelWeights, PickByWeightage pickByWeightage) {
            List<Integer> reelRowsPicked = new ArrayList<>(reelWeights.size());
            for (List<Integer> weights : reelWeights) {
                int rows = pickByWeightage.pickIndexedItem(weights);
                reelRowsPicked.add(rows);
            }
            return reelRowsPicked;
    }
}
