package com.feg.games.ClashOfMighty.ext.exceptions;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class DefaultErrorCode implements ErrorCode, Serializable {

    String code;
    String description;
    int httpStatusCode;

    @JsonCreator
    public DefaultErrorCode(String code, String description, int httpStatusCode) {
        this.code = code;
        this.description = description;
        this.httpStatusCode = httpStatusCode;
    }

    @Override
    public String getCode() {
        return code;
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
