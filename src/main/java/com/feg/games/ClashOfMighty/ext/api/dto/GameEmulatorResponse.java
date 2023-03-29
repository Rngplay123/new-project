package com.feg.games.ClashOfMighty.ext.api.dto;

import com.feg.games.ClashOfMighty.ext.api.model.EmulatorResult;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

@Data
public class GameEmulatorResponse {
    Integer spins;
    BigDecimal rtp;
    List<EmulatorResult> results;

    BigDecimal getRtp() {
        if (CollectionUtils.isEmpty(results)) {
            return null;
        } else {
            return results.stream().map(EmulatorResult::getRtp)
                    .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        }
    }
}
