package me.tofpu.speedbridge.api.model.service;

import me.tofpu.speedbridge.api.model.object.game.Result;
import me.tofpu.speedbridge.api.model.object.island.Island;
import me.tofpu.speedbridge.api.model.object.mode.Mode;
import me.tofpu.speedbridge.api.model.object.user.User;
import me.tofpu.speedbridge.api.model.object.user.timer.Timer;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * This handles everything game related. From joining to resetting islands back to their state.
 */
public interface GameService {
    Result join(final JoinAlgorithm algorithm, final Player player);

    /**
     * This method will teleport the issuer to the target
     * to spectate in their island.
     * <p></p>
     * The issuer will also get notified when the target scores
     * and whether the target have beaten their
     * personal score or not.
     * <p></p>
     * The issuer will also get teleported back to the lobby
     * once the target leaves/disconnect from the island.
     *
     * @param issuer the one whom issued the command
     * @param target the one whom the issuer wants to spectate to
     *
     * @return the result of the action
     * <p>
     * FULL - If the issuer is playing
     * <p>
     * FAIL - if the target wasn't playing
     * <p>
     * SUCCESS - If the action was successful
     */
    Result spectate(final Player issuer, final Player target);

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
     * This method is for checking if the player are playing or not.
     *
     * @param player the player you want to check against
     *
     * @return true if player is an island, otherwise false
     */
    boolean isPlaying(final Player player);

    /**
     * This method is for checking if the player are spectating or not.
     *
     * @param player the player you want to check against
     *
     * @return true if player is spectating, otherwise false
     */
    boolean isSpectating(final Player player);

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
    Optional<Timer> getTimer(final User user);

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

    enum JoinType {
        RANDOM, SELECTIVE, CATEGORY
    }

    public static class JoinAlgorithm {
        public static final JoinAlgorithm RANDOM = new JoinAlgorithm();
        public static JoinAlgorithm of(final int slot) {
            return new JoinAlgorithm(slot);
        }
        public static JoinAlgorithm of(final Mode mode) {
            return new JoinAlgorithm(mode);
        }

        private final JoinType type;
        private int slot;
        private Mode mode;

        private JoinAlgorithm() {
            this.type = JoinType.RANDOM;
        }

        private JoinAlgorithm(final int slot) {
            this.type = JoinType.SELECTIVE;
            this.slot = slot;
        }

        private JoinAlgorithm(final Mode mode) {
            this.type = JoinType.CATEGORY;
            this.mode = mode;
        }

        public JoinType type() {
            return this.type;
        }

        public int slot() {
            return this.slot;
        }

        public Mode mode() {
            return this.mode;
        }
    }
}
