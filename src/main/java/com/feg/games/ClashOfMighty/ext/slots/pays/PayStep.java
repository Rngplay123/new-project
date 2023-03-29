package com.feg.games.ClashOfMighty.ext.slots.pays;

import com.feg.games.ClashOfMighty.ext.slots.model.Cluster;
import com.feg.games.ClashOfMighty.ext.slots.model.PayLine;
import com.feg.games.ClashOfMighty.ext.slots.model.PayWay;
import com.feg.games.ClashOfMighty.ext.slots.model.SlotsPay;
import com.feg.games.ClashOfMighty.ext.slots.reels.SymbolGrid;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class PayStep {

    SymbolGrid stepSymbolGrid;
    private SlotsPay scatterPay;
    private List<PayLine> payLines;
    private List<PayWay> payWays;
    private List<Integer> topReelsIndexes;
    private BigDecimal stepWins;
    private List<Cluster> clusters;
    private Map<String,Object> data = new HashMap<>();

    public PayStep(SymbolGrid symbolGrid) {
        this.stepSymbolGrid = symbolGrid;
        this.topReelsIndexes = this.computeReelsTopSymbolIndex();
    }

    @SuppressWarnings("ConstantConditions")
    private List<Integer> computeReelsTopSymbolIndex() {
        List<Integer> topReelsIndexes = new ArrayList<>(getStepSymbolGrid().getReelCount());
        for (int reel = 0; reel < getStepSymbolGrid().getReelCount(); reel++) {
            topReelsIndexes.add(this.stepSymbolGrid.getSymbolIndexGrid().get(reel).get(0));
        }
        this.topReelsIndexes = topReelsIndexes;
        return topReelsIndexes;
    }
}
