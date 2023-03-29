package com.feg.games.ClashOfMighty.engine.jackson;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.feg.games.ClashOfMighty.engine.bonus.RngBonusContext;
import com.feg.games.ClashOfMighty.engine.bonus.RngGameBonus;
import com.feg.games.ClashOfMighty.engine.model.dto.RngGameEngineRequest;
import com.feg.games.ClashOfMighty.engine.symbols.RngSymbol;
import com.feg.games.ClashOfMighty.ext.api.bonus.BonusContext;
import com.feg.games.ClashOfMighty.ext.api.bonus.GameBonus;
import com.feg.games.ClashOfMighty.ext.api.model.GameActivity;
import com.feg.games.ClashOfMighty.ext.api.model.GameEngineRequest;
import com.feg.games.ClashOfMighty.ext.api.model.GameEngineResponse;
import com.feg.games.ClashOfMighty.ext.api.model.GamePlayState;
import com.feg.games.ClashOfMighty.ext.api.module.GameEngineModule;
import com.feg.games.ClashOfMighty.ext.api.symbol.Symbol;
import com.feg.games.ClashOfMighty.ext.slots.api.dto.SlotsGameEngineResponse;
import com.feg.games.ClashOfMighty.ext.slots.api.model.SlotsGamePlayState;
import com.feg.games.ClashOfMighty.ext.slots.model.SlotsSpinGameActivity;

import java.io.IOException;

public class EngineModule extends GameEngineModule {


    public EngineModule() {
        super(EngineModule.class.getSimpleName());
        this.addAbstractTypeMapping(GameEngineRequest.class, RngGameEngineRequest.class);
        this.addAbstractTypeMapping(GameEngineResponse.class, SlotsGameEngineResponse.class);
        this.addAbstractTypeMapping(GamePlayState.class, SlotsGamePlayState.class);
        this.addAbstractTypeMapping(GameActivity.class, SlotsSpinGameActivity.class);
        this.addAbstractTypeMapping(Symbol.class, RngSymbol.class);
        this.addAbstractTypeMapping(BonusContext.class, RngBonusContext.class);
        this.addAbstractTypeMapping(GameBonus.class, RngGameBonus.class);

        this.addKeyDeserializer(GameBonus.class, new KeyDeserializer() {
            @Override
            public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
                return Enum.valueOf(RngGameBonus.class, key);
            }
        });
    }
}
