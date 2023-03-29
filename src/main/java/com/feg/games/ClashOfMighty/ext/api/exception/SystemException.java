package com.feg.games.ClashOfMighty.ext.api.exception;


import com.feg.games.ClashOfMighty.ext.exceptions.ErrorCode;

import java.util.Map;
import java.util.TreeMap;

public class SystemException extends Exception {
    private static final long serialVersionUID = 1L;
    private final Map<String, Object> properties = new TreeMap<String, Object>();
    private ErrorCode errorCode;

    public SystemException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public SystemException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public SystemException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public SystemException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public SystemException setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }


    public <T> T get(String name) {
        return (T) properties.get(name);
    }

    public SystemException set(String name, Object value) {
        properties.put(name, value);
        return this;
    }

}
