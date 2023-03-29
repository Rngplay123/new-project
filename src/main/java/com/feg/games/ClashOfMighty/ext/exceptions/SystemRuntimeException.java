package com.feg.games.ClashOfMighty.ext.exceptions;

import lombok.Data;

/**
 * This is default run time exception.
 *
 * @author Suresh Reddy Guntaka
 */
@Data
public class SystemRuntimeException extends RuntimeException implements CustomErrorCode {

    private final ErrorCode errorCode;

    /**
     * Construct a {@code SystemRuntimeException} with the specified detail message.
     *
     * @param errorCode errorCode this should have error code enum value
     */
    public SystemRuntimeException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }

    /**
     * Construct a {@code SystemRuntimeException} with the specified detail message.
     *
     * @param errorCode errorCode this should have error code enum value
     */
    public SystemRuntimeException(ErrorCode errorCode, String message) {
        super(errorCode.getDescription() + ":" + message);
        this.errorCode = errorCode;
    }


    /**
     * Construct a {@code SystemRuntimeException} with the specified detail message
     * and nested exception.
     *
     * @param errorCode errorCode this should have error code enum value
     * @param cause     the nested exception
     */
    public SystemRuntimeException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getDescription(), cause);
        this.errorCode = errorCode;
    }


    /**
     * Construct a {@code SystemRuntimeException} with the specified detail message
     * and nested exception.
     *
     * @param errorCode errorCode this should have error code enum value
     * @param cause     the nested exception
     */
    public SystemRuntimeException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode.getDescription() + ":" + message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Factory method to create RAD runtime exception.
     *
     * @param errorCode - errorcode with which exception needs to be created.
     * @return - returns RAD exception.
     */
    public static SystemRuntimeException of(ErrorCode errorCode) {
        return new SystemRuntimeException(errorCode);
    }
}