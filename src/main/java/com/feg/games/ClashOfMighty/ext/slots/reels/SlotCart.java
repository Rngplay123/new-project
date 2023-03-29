/*
package com.rngplay.gaming.slots.reels;

import com.rngplay.gaming.slots.api.error.SlotsErrorCode;

import com.rngplay.gaming.slots.symbol.SlotSymbol;
import com.rngplay.gaming.ext.utils.slots.NumberUtils;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.SortedSet;
import java.util.concurrent.atomic.AtomicInteger;

*/
/**
 * The SlotCart.
 *//*

@Data
public class SlotCart<TSymbol extends SlotSymbol> {
    private Integer cartSize;
    private List<TSymbol> cartSymbols = new ArrayList<>();
    private List<Integer> cartIndexes = new ArrayList<>();
    private SlotReel<TSymbol> cartRefReel;
    private Integer cartStartPosition;


    public SlotCart(SlotCart<TSymbol> slotCart) {
        this.cartSize = slotCart.getCartSize();
        this.cartSymbols.addAll(slotCart.getCartSymbols());
        this.cartIndexes.addAll(slotCart.getCartIndexes());
        this.cartRefReel = slotCart.getCartRefReel();
        this.cartStartPosition = slotCart.getCartStartPosition();
    }

    public SlotCart(int start, int cartSize, SlotReel<TSymbol> cartRefReel) {
        this.cartSize = cartSize;
        this.cartStartPosition = start;
        this.cartRefReel = cartRefReel;
        this.populateCart();
    }

    private void populateCart() {
        cartIndexes = NumberUtils.getSequenceList(cartStartPosition, cartSize, cartRefReel.getSymbols().size());
        cartSymbols = new ArrayList<>(cartIndexes.size());
        List<TSymbol> cartRefSymbols = cartRefReel.getSymbols();
        cartIndexes.forEach(index -> cartSymbols.add(cartRefSymbols.get(index)));
    }

    public boolean cascade(SortedSet<Integer> positions) throws GameEngineException {
        if (CollectionUtils.isEmpty(positions)) {
            return false;
        }

        for (int currentPos : positions) {
            if (currentPos >= this.cartSize)
                throw new UnsupportedOperationException(
                        "Invalid Cascade position (" + currentPos
                                + ") greater than or equal to cart size (" + this.cartSize + ")");
        }

        CascadeDirection cascadeDirection = cartRefReel.getCascade();
        int cascadePosition;
        if (cascadeDirection == CascadeDirection.TOP) {
            cascadePosition = getTopIndex();
        } else if (cascadeDirection == CascadeDirection.BOTTOM) {
            cascadePosition = getBottomIndex();
        } else {
            throw new GameEngineException(EngineErrorCode.INTERNAL_ERROR);
        }

        int removed = 0;
        for (int currentPos : positions) {
            this.cartIndexes.set(currentPos, -1);
            this.cartSymbols.set(currentPos, null);
            removed++;
        }

        this.cartIndexes.removeIf(index -> index == -1);
        this.cartSymbols.removeIf(Objects::isNull);

        AtomicInteger currentIndex = new AtomicInteger(cascadePosition);
        while (removed != 0) {
            int nextIndex = nextCartIndex(currentIndex, cascadeDirection);
            currentIndex.set(nextIndex);
            addSymbolForSymbolIndex(nextIndex, cascadeDirection);
            removed--;
        }

        cartStartPosition = this.cartIndexes.get(0);
        return true;
    }

    private void addSymbolForSymbolIndex(int newIndex, CascadeDirection cascadeDirection) {
        TSymbol newSym = cartRefReel.getSymbols().get(newIndex);
        if (cascadeDirection == CascadeDirection.TOP) {
            this.cartIndexes.add(0, newIndex);
            this.cartSymbols.add(0, newSym);
        } else {
            this.cartIndexes.add(newIndex);
            this.cartSymbols.add(newSym);
        }
    }

    private int nextCartIndex(AtomicInteger current, CascadeDirection cascade) throws GameEngineException {
        int currentPos;
        if (cascade == CascadeDirection.TOP)
            currentPos = current.decrementAndGet();
        else if (cascade == CascadeDirection.BOTTOM)
            currentPos = current.incrementAndGet();
        else {
            throw new GameEngineException("wrong Cascade direction used : " + cascade, EngineErrorCode.INTERNAL_ERROR);
        }

        return Math.floorMod(currentPos, cartRefReel.getSymbols().size());
    }


    private int getTopIndex() {
        return this.cartIndexes.get(0);
    }

    private int getBottomIndex() {
        return this.cartIndexes.get(this.cartSize - 1);
    }

    public boolean getSymbolPositions(TSymbol symbol, int cartIndex, List<TSymbol> wilds) {

        if (cartIndex < 0 || (cartIndex >= cartSymbols.size())) {
            return false;
        }

        TSymbol target = this.cartSymbols.get(cartIndex);
        return target.equals(symbol) || wilds.contains(target);
    }
}
*/
