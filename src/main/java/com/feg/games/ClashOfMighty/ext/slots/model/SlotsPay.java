package com.feg.games.ClashOfMighty.ext.slots.model;

import com.feg.games.ClashOfMighty.ext.api.symbol.Symbol;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The SlotsPay.
 */
@Data
@NoArgsConstructor
public class SlotsPay {
    private Symbol symbol;
    private BigDecimal multiplier = BigDecimal.ONE;
    private BigDecimal bonusMultiplier = BigDecimal.ONE;
    private List<List<Integer>> reelSymbolPositions;
    private Integer symbolCount;
    private BigDecimal winnings;
    private Map<String, Object> data = new HashMap<>();
    public static final Integer SCALE;
    public static final RoundingMode ROUNDING_MODE;

    static {
        SCALE = Integer.valueOf(System.getProperty("engine.amounts.scale", "2"));
        ROUNDING_MODE = RoundingMode.valueOf(System.getProperty("engine.amounts.roundingMode", "HALF_UP"));
    }

    public SlotsPay(@NonNull Symbol symbol, @NonNull BigDecimal multiplier, @NonNull Integer symbolCount) {
        this.symbol = symbol;
        this.multiplier = multiplier;
        this.symbolCount = symbolCount;
    }

    public SlotsPay(@NonNull Symbol symbol, @NonNull BigDecimal multiplier, @NonNull List<List<Integer>> reelSymbolPositions, @NonNull Integer symbolCount) {
        this(symbol, multiplier, symbolCount);
        this.reelSymbolPositions = reelSymbolPositions;
    }


    public SlotsPay(@NonNull Symbol symbol, @NonNull BigDecimal multiplier, @NonNull BigDecimal bonusMultiplier, @NonNull List<List<Integer>> reelSymbolPositions, @NonNull Integer symbolCount) {
        this(symbol, multiplier, symbolCount);
        this.bonusMultiplier = bonusMultiplier;
        this.reelSymbolPositions = reelSymbolPositions;
    }

    public SlotsPay(@NonNull Symbol symbol, @NonNull BigDecimal multiplier, @NonNull BigDecimal bonusMultiplier, @NonNull Integer symbolCount) {
        this(symbol, multiplier, symbolCount);
        this.bonusMultiplier = bonusMultiplier;
    }

    public static BigDecimal totalWins(@NonNull Collection<SlotsPay> slotPays, BigDecimal stake) {
        return slotPays.stream()
                .map(payLine -> stake
                        .multiply(payLine.winsMultiplier()).setScale(SCALE, SlotsPay.ROUNDING_MODE))
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    public BigDecimal winsMultiplier() {
        return multiplier.multiply(this.bonusMultiplier).setScale(SCALE, SlotsPay.ROUNDING_MODE);
    }


    public enum PayType {
        LINES,
        WAYS
    }
}
