package com.feg.games.ClashOfMighty.ext.slots.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.feg.games.ClashOfMighty.ext.api.symbol.Symbol;
import com.feg.games.ClashOfMighty.ext.api.symbol.SymbolType;
import jakarta.annotation.Nonnull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * The SymbolLine.
 */
@Data
@NoArgsConstructor

public class SymbolLine implements Iterable<Symbol> {

    private Symbol paySymbol;

    private int[] payLineOffSet;

    private List<Symbol> symbols = new ArrayList<>();

    private LineDirection lineDirection;

    public SymbolLine(int... payLineOffset) {
        this.payLineOffSet = payLineOffset;
    }

    public SymbolLine(SymbolLine symbolLine) {
        this(symbolLine.payLineOffSet);
        symbolLine.symbols.forEach(s -> {
            this.getSymbols().add(s);
        });
        this.setLineDirection(symbolLine.getLineDirection());
    }

    public SymbolLine(List<Symbol> lineSymbols) {
        this.symbols.addAll(lineSymbols);
    }

    public int size() {
        return symbols.size();
    }

    public Symbol get(int index) {
        return symbols.get(index);
    }

    public Symbol first() {
        return symbols.get(0);
    }

    public Symbol last() {
        return symbols.get(symbols.size() - 1);
    }

    public void append(Symbol symbol) {
        symbols.add(symbol);
    }


    @Nonnull
    @Override
    public Iterator<Symbol> iterator() {
        return symbols.iterator();
    }


    public Symbol getPaySymbol(LineDirection lineDirection) {

        if (lineDirection == LineDirection.RIGHT) {
            ListIterator<Symbol> listIterator = symbols.listIterator(symbols.size());
            Symbol symbol;
            while (listIterator.hasPrevious()) {
                symbol = listIterator.previous();
                if (symbol.getSymbolType().contains(SymbolType.NORMAL)) {
                    this.paySymbol = symbol;
                    break;
                }
            }
        } else {
            for (Symbol symbol : this) {
                if (symbol.getSymbolType().contains(SymbolType.NORMAL)) {
                    this.paySymbol = symbol;
                    break;
                }
            }
        }
        return this.paySymbol;
    }

    public Symbol getPaySymbol() {
        if (this.paySymbol != null)
            return this.paySymbol;
        return getPaySymbol(lineDirection);
    }

    public void replace(int i, Symbol symbol) {
        symbols.set(i, symbol);
    }

    public int getKindASymbolCount(boolean adjacent) {

        int count = 0;
        if (lineDirection == LineDirection.LEFT) {
            Symbol paySymbol = this.paySymbol!=null?this.paySymbol:first();
            for (Symbol symbol : symbols) {
                if (!paySymbol.equals(symbol)) {
                    if (adjacent)
                        break;
                    else
                        continue;
                }

                if (paySymbol.equals(symbol))
                    count++;
            }
        } else {
            Symbol paySymbol = last();
            ListIterator<Symbol> listIterator = symbols.listIterator(symbols.size());
            Symbol symbol = null;
            while (listIterator.hasPrevious()) {
                symbol = listIterator.previous();
                if (!symbol.equals(paySymbol)) {
                    if (adjacent)
                        break;
                    else
                        continue;
                }

                if (paySymbol.equals(symbol))
                    count++;
            }
        }

        return count;
    }


    public SymbolLine replaceSymbols(List<? extends Symbol> from, List<? extends Symbol> dontReplace, Symbol to) {
        if (from == null || to == null)
            throw new IllegalArgumentException("from: " + from + ", to: " + to);

        SymbolLine symbolsLine = new SymbolLine(getSymbols());
        symbolsLine.setPaySymbol(to);
        for (int i = 0; i < symbolsLine.size(); i++) {
            Symbol candidate = symbolsLine.get(i);
            if (!dontReplace.contains(from) && from.contains(candidate)) {
                symbolsLine.replace(i, to);
            }
        }
        return symbolsLine;
    }

    public BigDecimal calculateMultiplier(Symbol paySymbol, boolean adjacent,
                                          LinkedMultiValueMap<? extends Symbol, BigDecimal> symbolStakeMultipliers) {
        if (symbolStakeMultipliers == null) {
            throw new IllegalArgumentException("symbolStakeMultiplierConfiguration should not be null");
        }
        int symbolCount = getKindASymbolCount(adjacent);
        return selectMultiplier(paySymbol, symbolCount, symbolStakeMultipliers);
    }


    private BigDecimal selectMultiplier(Symbol symbol, int symbolCount,
                                        LinkedMultiValueMap<? extends Symbol,
                                                BigDecimal> symbolStakeMultipliers) {
        if(symbolCount==0)
            return BigDecimal.ZERO;

        List<BigDecimal> multipliersForSymbol = symbolStakeMultipliers.get(symbol);

        if (multipliersForSymbol == null) {
            return BigDecimal.ZERO;
        }

        if (multipliersForSymbol.size() < symbolCount) {
            throw new IllegalArgumentException("No multiplier defined, scatterSymbol: " + symbol
                                               + ", symbolCount: " + symbolCount);
        }
        return multipliersForSymbol.get(symbolCount - 1);
    }

    public boolean isWildContributingToWins(SymbolLine symbolLine, boolean adjacent) {
        int kindASymCount = getKindASymbolCount(adjacent);
        for (int index = 0; index < kindASymCount; index++) {
            if (symbolLine.get(index).getSymbolType().contains(SymbolType.WILD)) {
                return true;
            }
        }
        return false;
    }

    public boolean isWildContributingToWins(SymbolLine symbolLine, Symbol wild, boolean adjacent) {
        int kindASymCount = getKindASymbolCount(adjacent);
        for (int index = 0; index < kindASymCount; index++) {
            if (symbolLine.get(index) == wild) {
                return true;
            }
        }
        return false;
    }

    public enum LineDirection {
        LEFT,
        RIGHT,
        BOTH,
    }

}
