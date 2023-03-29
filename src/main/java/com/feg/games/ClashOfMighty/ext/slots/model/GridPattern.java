package com.feg.games.ClashOfMighty.ext.slots.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sureshreddy on 01/06/17.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GridPattern<T extends GridPatternItem> implements Serializable {

    private List<T> items = new ArrayList<>();

    public GridPattern(GridPattern<T> gridPattern) {
        this.items.addAll(gridPattern.getItems());
    }

    public void setItems(List<T> items) {

        Map<String, T> positionMap = new HashMap<>();
        for (T item : items) {
            String key = String.format("%d:%d", item.getRow(), item.getColumn());
            if (positionMap.get(key) != null) {
                throw new IllegalArgumentException(String.format("Multiple items with identical grid positions are not allowed, row: %d, column: %d", item.getRow(), item.getColumn()));
            }
            positionMap.put(key, item);
        }
        positionMap = null;

        this.items = items;
    }

    public void addItem(T gridPatternItem) {
        T existingItem = getAt(gridPatternItem.getRow(), gridPatternItem.getColumn());
        if (existingItem != null) {
            throw new IllegalArgumentException(String.format("An item already exists in the specified position, row: %d, column: %d", gridPatternItem.getRow(), gridPatternItem.getColumn()));
        }
        items.add(gridPatternItem);
    }

    public T getAt(Integer row, Integer column) {
        for (T item : items) {
            if (item.getRow().equals(row) && item.getColumn().equals(column)) {
                return item;
            }
        }
        return null;
    }
}
