package game.items.weapons.coating;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * Coating applied to a weapon. It triggers extra effects when the weapon hits.
 */
public interface Coating {
    /** Coating name for display (e.g., "Yewberry", "Snow"). */
    String name();

    /**
     * Called when the coated weapon successfully hits a target.
     *
     * @return
     */
    String onHit(Actor attacker, Actor target, GameMap map);
}
