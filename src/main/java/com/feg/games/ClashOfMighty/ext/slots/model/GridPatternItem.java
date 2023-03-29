package com.feg.games.ClashOfMighty.ext.slots.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by sureshreddy on 01/06/17.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GridPatternItem implements Serializable {

    private Integer row = 0;

    private Integer column = 0;

}