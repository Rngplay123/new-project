package com.feg.games.ClashOfMighty.ext.api.exception;

public class GameEngineException extends RuntimeException{

    private String message;


    public GameEngineException(String message) {
        super( message);
    }

    public GameEngineException(Throwable cause) {
        super(cause);
    }

    public GameEngineException(String message, Throwable cause) {
        super(message, cause);
    }
}

