package me.tofpu.speedbridge.api.game;

import me.tofpu.speedbridge.api.island.Island;
import me.tofpu.speedbridge.api.island.mode.Mode;
import me.tofpu.speedbridge.api.user.User;
import me.tofpu.speedbridge.api.user.timer.Timer;
import org.bukkit.entity.Player;

/**
 * This handles everything game related. From joining to resetting islands back to their state.
 */
public interface GameService {
    /**
     * This method will look for any available island
     * and have the player join the island.
     *
     * @param player the player instance that trying to join
     *
     * @return the result of the action
     * @see #join(Player, Mode)
     */
    Result join(final Player player);

    /**
     * This method will look for an island available
     * associated with this slot.
     *
     * @param player the player instance that's trying to join
     * @param slot the island slot that the player is trying to join
     *
     * @return the result of the action
     * <p>
     * INVALID_LOBBY - if the lobby location were not defined
     * <p>
     * INVALID_ISLAND - if the island instance didn't exist
     * <p>
     * FULL - if the island is already taken
     * <p>
     * DENY - if the player instance is null (shouldn't happen)
     * <p>
     * SUCCESS - If the action was successful
     * @see Result
     */
    Result join(final Player player, final int slot);

    /**
     * This method will look for an island associated with this mode.
     * <p></p>
     * Here is the current joining process:
     * <p>
     * If the mode were null, it'll try the default mode defined by the settings
     * and if the default mode were still null, it'll look for any other available
     * island for the player to join.
     *
     * @param player the player instance
     * @param mode the mode instance
     *
     * @return the result of the action
     * <p>
     * INVALID_LOBBY - if the lobby location were not defined
     * <p>
     * INVALID_ISLAND - if the island instance didn't exist
     * <p>
     * DENY - if the player instance is null (shouldn't happen)
     * <p>
     * FULL - if the mode defined slots islands were all taken
     * <p>
     * NONE - If there is no available island, at all
     * <p>
     * SUCCESS - If the action was successful
     * @see Result
     */
    Result join(final Player player, final Mode mode);

    /**
     * This method is for kicking the player out of the island and
     * teleporting them back to the lobby's location.
     *
     * @param player the player instance
     *
     * @return the result of the action
     * DENY if an instance of user didn't exist (reloaded maybe?)
     * <p>
     * otherwise, SUCCESS
     * @see Result
     */
    Result leave(final Player player);

    /**
     * This method is for checking if the player were playing or not.
     *
     * @param player the player instance
     *
     * @return true if player is an island, otherwise false
     */
    boolean isPlaying(final Player player);

    /**
     * This method is for adding a brand new timer
     * to the list and have it start ticking.
     *
     * @param user the user instance
     */
    void addTimer(final User user);

    /**
     * This method is for checking if this user
     * has started their ticking timer.
     *
     * @param user the user instance
     *
     * @return true if the timer has started ticking (the user was in the timer's cache), otherwise false
     */
    boolean hasTimer(final User user);

    /**
     * This method is for getting a cached instance of
     * this user's timer instance.
     *
     * @param user the user instance
     *
     * @return a cached timer associated with this user
     */
    Timer getTimer(final User user);

    /**
     * This method is for primarily checking against the cached timer
     * and the personal best beaten record.
     * <p></p>
     * it'll update the player personal best record accordingly.
     *
     * @param user the user instance
     */
    void updateTimer(final User user);

    /**
     * This method is for resetting this user's timer.
     *
     * @param user the user instance
     */
    void resetTimer(final User user);

    /**
     * This method is for resetting the island and
     * teleporting the user back to the island's location
     * for them to start over.
     *
     * @param user the user instance
     */
    void reset(final User user);

    /**
     * this method is for primarily resetting
     * the island to it's original state.
     *
     * @param island the island instance
     */
    void resetBlocks(final Island island);

    /**
     * This method is for resetting the island
     * back to it's original state and making it available
     * for anyone to join.
     *
     * @param slot the island's slot
     */
    void resetIsland(final int slot);
}
