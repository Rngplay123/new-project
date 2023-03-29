package com.feg.games.ClashOfMighty.ext.exceptions;

import lombok.Data;

/**
 * This is default exception
 *
 * @author Suresh Reddy Guntaka
 */
@Data
public class SystemException extends Exception implements CustomErrorCode {

    private final ErrorCode errorCode;

    /**
     * Construct a {@code SystemException} with the specified error code.
     *
     * @param errorCode errorCode this should have error code enum value
     */
    public SystemException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }

    /**
     * Construct a {@code SystemException} with the specified error code.
     *
     * @param errorCode errorCode this should have error code enum value
     */
    public SystemException(ErrorCode errorCode, String message) {
        super(errorCode.getDescription() + ":" + message);
        this.errorCode = errorCode;
    }

    /**
     * Construct a {@code SystemException} with the specified error code
     * and nested exception.
     *
     * @param errorCode errorCode this should have error code enum value
     * @param cause     the nested exception
     */
    public SystemException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getDescription(), cause);
        this.errorCode = errorCode;
    }


    /**
     * Construct a {@code SystemException} with the specified error code
     * and nested exception.
     *
     * @param errorCode errorCode this should have error code enum value
     * @param cause     the nested exception
     */
    public SystemException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode.getDescription() + ":" + message, cause);
        this.errorCode = errorCode;
    }


    /**
     * Factory method to create RAD  exception.
     *
     * @param errorCode - errorcode with which exception needs to be created.
     * @return - returns RAD exception.
     */
    public static SystemException of(ErrorCode errorCode) {
        return new SystemException(errorCode);
    }
}