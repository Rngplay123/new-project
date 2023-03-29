package com.feg.games.ClashOfMighty.ext.api.model;

public enum GameType {
    SLOTS,
    CASCADE,
    TABLE,
    WHEEL,
    BOARD,
    INSTANT_WIN,
    LOTTERY,
    BINGO;



    private GameType() {
    }

    public String toString() {
        return this.name();
    }

    public interface TypeConstants {
        String SLOTS = "SLOTS";
       String CASCADE = "CASCADE";
       String TABLE = "TABLE";
       String WHEEL = "WHEEL";
       String BOARD = "BOARD";
       String INSTANT_WIN = "INSTANT_WIN";
       String LOTTERY = "LOTTERY";
       String BINGO = "BINGO";
    }
}
