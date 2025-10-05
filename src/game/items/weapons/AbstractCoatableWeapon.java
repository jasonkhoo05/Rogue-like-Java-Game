package game.items.weapons;

import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.weapons.Weapon;

/**
 * Base class for weapons that can be coated with various effects (e.g., snow, yewberry).
 * Torch is excluded from this system.
 */
public abstract class AbstractCoatableWeapon extends Item implements Weapon {

    /** Current coating applied to this weapon, or null if none. */
    private Coating coating;

    /**
     * Constructor.
     *
     * @param name the weapon's name
     * @param displayChar the display character for the weapon
     */
    protected AbstractCoatableWeapon(String name, char displayChar) {
        super(name, displayChar, true); // true = portable item
    }

    /** Whether this weapon type supports coatings. Torch will override to return false. */
    public boolean isCoatable() {
        return true;
    }

    /** Apply or replace a coating. */
    public void coatWith(Coating newCoating) {
        this.coating = newCoating;
    }

    /** Trigger the coating effect when the weapon hits a target. */
    protected void triggerCoating(Actor attacker, Actor target, GameMap map) {
        if (coating != null) {
            coating.onHit(attacker, target, map);
        }
    }

    /** Display coating name if present (e.g., "Axe [Yewberry]"). */
    @Override
    public String toString() {
        if (coating == null) {
            return super.toString();
        }
        return super.toString() + " [" + coating.name() + "]";
    }
}

