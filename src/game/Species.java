
package game;

/**
 * Enumerates the supported animal species in the game.
 * <p>
 * This enum is used as an ability/marker (e.g., via {@code enableAbility(Species.X)})
 * so behaviours and effects can target species without {@code instanceof} checks.
 */
public enum Species {
    /** Brown/forest predator; may trigger yew-berry scatter on spawn. */
    BEAR,

    /** Agile predator; may grow a special proximity yewberry tree on spawn. */
    WOLF,

    /** Herbivore; may cause an apple to drop on a neighbouring tile on spawn. */
    DEER,

    /** Apex reptile; on spawn may apply a poison aura to nearby actors. */
    CROCODILE
}
