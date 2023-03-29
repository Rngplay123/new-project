package com.feg.games.ClashOfMighty.ext.slots.utils;

import com.feg.games.ClashOfMighty.engine.model.RNG;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The NumberUtils.
 */
public class NumberUtils {
    @Autowired
    static
    RNG rng;

    //static RNG rng = new Fortuna();

    public static RNG getRng() {
        return rng;
    }

    public static void setRng(RNG rng) {
        NumberUtils.rng = rng;
    }

    public static List<Integer> getSequenceList(int number, int size, int maxNumber) {
        List<Integer> sequenceList = new ArrayList<>(size);
        AtomicInteger currentIndex = new AtomicInteger(number);
        while (size != 0) {
            Integer nextIndex = Math.floorMod(currentIndex.getAndIncrement(), maxNumber);
            sequenceList.add(nextIndex);
            size--;
        }
        return sequenceList;
    }


    public static SortedSet<Integer> getUniqueRandomList(int maxNumber) {
        TreeSet<Integer> sequenceList = new TreeSet<>();
        Set<Integer> picked = new HashSet<>();
        int pick = rng.nextInt(maxNumber);

        while (pick == 0) {
            pick = rng.nextInt(maxNumber);
        }

        while (pick != 0) {
            int num = rng.nextInt(maxNumber);
            if (!picked.contains(num)) {
                sequenceList.add(num);
                picked.add(num);
                pick--;
            }
        }
        return sequenceList;
    }
}
