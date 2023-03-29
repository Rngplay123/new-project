package com.feg.games.ClashOfMighty.engine.model;

import lombok.Data;

import java.util.List;

/**
 * The ROAGameConfiguration.
 */
@Data
public class RtpInfo {
    String mainGame;
    List<BuyRtpInfo> buyFeature;
}
