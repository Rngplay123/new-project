package com.feg.games.ClashOfMighty.ext.api.error;

import com.feg.games.ClashOfMighty.ext.exceptions.ErrorCode;
import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EngineErrorCode implements ErrorCode {
    INTERNAL_ERROR("100", "Internal Error", 500),
    SUCCESS("000", "Success", 200),
    INVALID_REQUEST("101", "Invalid Request", 400),
    INVALID_GAME_STATE("102", "Invalid game state", 400),
    INVALID_GAME_CONFIGURATION("103", "Invalid game configuration", 500),
    INVALID_STAKE("104", "Invalid stake", 400);

    private final String code;
    private final String description;
    private final int httpStatusCode;

    EngineErrorCode(String code, String description, int httpStatusCode) {
        this.code = code;
        this.description = description;
        this.httpStatusCode = httpStatusCode;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getHttpStatusCode() {
        return httpStatusCode;
    }
}
