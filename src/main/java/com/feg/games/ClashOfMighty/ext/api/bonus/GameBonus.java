package com.feg.games.ClashOfMighty.ext.api.bonus;

/**
 * Interface to all Bonus types.
 */
public interface GameBonus {

    /**
     * Indicates the game engines provider to continue game round when bonus awarded.
     *
     * @return boolean
     */
    boolean continueGameRound();

    /**
     * Bonus Name.
     *
     * @return String representation Bonus.
     */
    String getName();
}
