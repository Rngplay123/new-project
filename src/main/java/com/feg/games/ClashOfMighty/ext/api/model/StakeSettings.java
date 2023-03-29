package com.feg.games.ClashOfMighty.ext.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.io.Serializable;

/**
 * The StakeSettings.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StakeSettings implements Serializable {

    double[] minMax;
    @Nullable
    Double defaultStake;
    @Nullable
    double[] ladder;
    Integer[] minMaxLines;
}
