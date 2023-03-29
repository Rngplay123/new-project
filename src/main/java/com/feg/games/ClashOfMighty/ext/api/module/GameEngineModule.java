package com.feg.games.ClashOfMighty.ext.api.module;

import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class GameEngineModule extends SimpleModule {

    private Map<Class<?>, Class<?>> subTypesMap = new HashMap<>();

    public GameEngineModule(String name) {
        super(name);
    }


    @Override
    public <T> SimpleModule addAbstractTypeMapping(Class<T> superType, Class<? extends T> subType) {
        super.addAbstractTypeMapping(superType, subType);
        this.subTypesMap.put(superType, subType);
        return this;
    }

    @SuppressWarnings({"unchecked", "SuspiciousMethodCalls"})
    public <K, V extends K> V getSubType(K tClass) {
        return (V) this.subTypesMap.get(tClass);
    }
}
