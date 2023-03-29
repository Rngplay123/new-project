package com.feg.games.ClashOfMighty.ext.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * The GameConfiguration.
 */
@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class GameConfiguration implements Serializable {

    String mathModel;

    StakeSettings stakeSettings;
}
