package com.feg.games.ClashOfMighty.ext.slots.model;
import com.feg.games.ClashOfMighty.ext.api.symbol.Symbol;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
public class Cluster extends SlotsPay {

    private List<Integer> clusterPositions = Collections.emptyList();

    public Cluster(Symbol symbol,
                   BigDecimal multiplier,
                   BigDecimal bonusMultiplier,
                   List<Integer> clusterPositions,
                   int count
    ) {
        super(symbol, multiplier, bonusMultiplier, count);
        this.clusterPositions = clusterPositions;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Cluster{");
        sb.append(super.toString());
        sb.append("clusterPositions=").append(clusterPositions);
        sb.append('}');
        return sb.toString();
    }
}
