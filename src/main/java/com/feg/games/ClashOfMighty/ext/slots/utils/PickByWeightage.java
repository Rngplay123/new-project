package com.feg.games.ClashOfMighty.ext.slots.utils;

import com.feg.games.ClashOfMighty.engine.model.RNG;
import com.feg.games.ClashOfMighty.ext.api.exception.GameEngineException;
import lombok.Data;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * The PickByWeightage.
 */


@Component
@Data
public class PickByWeightage {

    @Autowired
    private RNG rng;

    public <T> List<T> pickRandomNItems(List<T> items, Integer n) {

        List<T> available = new LinkedList<>(items);

        List<T> picked = new LinkedList<>();
        while (picked.size() != n) {
            int itemIndex = rng.nextInt(available.size());
            picked.add(available.remove(itemIndex));
        }
        return picked;
    }

    public <T extends Weighted> T pick(@NonNull List<T> items,int pick) throws GameEngineException {
        if (CollectionUtils.isEmpty(items))
            throw new GameEngineException("No items to pick!");

        if (items.size() == 1)
            return items.get(0);

        int total = items.stream()
                .map(Weighted::getWeight)
                .reduce(Integer::sum)
                .orElse(0);
//        int pick;
        if (total < 1)
            return items.get(0);

        int counter = 0;
        Iterator<T> itr = items.iterator();
        T picked = null;
        while (itr.hasNext()) {
            T item = itr.next();
            counter = counter + item.getWeight();
            if (counter > pick) {
                picked = item;
                break;
            }
        }

        return picked;
    }

    public <T extends Weighted> T pick(@NonNull List<T> items) throws GameEngineException {
        if (CollectionUtils.isEmpty(items))
            throw new GameEngineException("No items to pick!");

        if (items.size() == 1)
            return items.get(0);

        int total = items.stream()
                .map(Weighted::getWeight)
                .reduce(Integer::sum)
                .orElse(0);
        int pick;
        if (total > 0)
            pick = rng.nextInt(total);
        else
            return items.get(0);

        int counter = 0;
        Iterator<T> itr = items.iterator();
        T picked = null;
        while (itr.hasNext()) {
            T item = itr.next();
            counter = counter + item.getWeight();
            if (counter > pick) {
                picked = item;
                break;
            }
        }

        return picked;
    }

    public Integer pickIndexedItem(@NonNull List<Integer> indexedItem) {

        int total = indexedItem.stream()
                .reduce(Integer::sum)
                .orElse(0);
        int pick;
        if (total > 0)
            pick = rng.nextInt(total);
        else
            pick = 0;

        int counter = 0;
        Iterator<Integer> itr = indexedItem.iterator();
        Integer pickedIndex = 0;
        while (itr.hasNext()) {
            Integer item = itr.next();
            counter = counter + item;
            if (counter > pick) {
                break;
            }
            pickedIndex++;
        }

        return pickedIndex;
    }


    public <T> T pickFromWeightedMap(Map<Integer, T> weightedMap) {

        int total = weightedMap.keySet().stream()
                .reduce(Integer::sum)
                .orElse(0);

        int pick;
        if (total > 0)
            pick = rng.nextInt(total);
        else
            return weightedMap.get(0);

        int counter = 0;
        Iterator<Map.Entry<Integer, T>> itr = weightedMap.entrySet().iterator();
        T pickedItem = null;
        while (itr.hasNext()) {
            Map.Entry<Integer, T> item = itr.next();
            counter = counter + item.getKey();
            if (counter > pick) {
                pickedItem = item.getValue();
                break;
            }
        }
        return pickedItem;
    }

    public <T> T pickFromMapWithValuesAsWeights(Map<T, Integer> weightedMap) {

        int total = weightedMap.values().stream()
                .reduce(Integer::sum)
                .orElse(0);

        int pick;
        if (total > 0)
            pick = rng.nextInt(total);
        else
            return null;

        int counter = 0;
        Iterator<Map.Entry<T, Integer>> itr = weightedMap.entrySet().iterator();
        T pickedItem = null;
        while (itr.hasNext()) {
            Map.Entry<T, Integer> item = itr.next();
            counter = counter + item.getValue();
            if (counter > pick) {
                pickedItem = item.getKey();
                break;
            }
        }
        return pickedItem;
    }
}
