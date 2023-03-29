package com.feg.games.ClashOfMighty.ext.exceptions;

import org.apache.http.HttpStatus;
/**
 * System errors enumeration .
 *
 * @author Suresh Reddy Guntaka
 * @since 1.0.0
 */
//@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SystemErrors implements ErrorCode {

    /**
     * System Error.
     */
    SYSTEM_ERROR("server.internal.error", "System Error", 500),

    /**
     * Uniqueness Error.
     */
    UNIQUE_CONSTRAINT_ERROR("unique.constraint.error", "Uniqueness Error ", 400),
    /**
     * Not found
     */
    NOT_FOUND_ERROR("not.found", "Not found", 404),

    /**
     * Entity Not Found
     */
    ENTITY_NOT_FOUND("entity.not.found", "Entity not found", 400),

    /**
     * Token Expired
     */
    TOKEN_EXPIRED("token.expired", "Token Expired", 403),

    /**
     * Invalid Token
     */
    INVALID_TOKEN("invalid.token", "Invalid Token", 400),
    /**
     * Com Error.
     */
    COM_ERROR("com.error", "Com Error", 500),
    /**
     * Throws exception
     */
    NOT_IMPLEMENTED_ERROR("not.implemented", "Not Implemented", 500),
    /**
     * Throws exception
     */
    NO_RESPONSE("no.response", "Not Response", 500),
    /**
     * Invalid Request
     */
    INVALID_REQUEST("invalid.request", "Invalid Request ", 400),

    /**
     * Throws access denied error
     */
    ACCESS_DENIED("access.denied", "Access Denied", HttpStatus.SC_FORBIDDEN);

    String code;
    String description;
    int httpStatusCode;

    SystemErrors(String code, String description, int httpStatusCode) {
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
        return this.description;
    }

    @Override
    public int getHttpStatusCode() {
        return this.httpStatusCode;
    }

}