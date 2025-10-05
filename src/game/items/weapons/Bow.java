package game.items.weapons;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;

/**
 * Bow (c) - a carryable Weapon item.
 * Has a 25% chance to hit and deals 5 damage.
 * Maximum range: 3 tiles.
 * Can be coated with additional effects (e.g., snow, yewberry).
 */
public class Bow extends AbstractCoatableWeapon {
    private static final int DAMAGE = 5;
    private static final int HIT_RATE = 25; // %
    private static final int RANGE = 3;     // max attack distance

    public Bow() {
        super("Bow", 'c'); // handled as portable in parent class
    }

    @Override
    public String attack(Actor attacker, Actor target, GameMap map) {
        Location attackerLoc = map.locationOf(attacker);
        Location targetLoc = map.locationOf(target);
        int dx = Math.abs(attackerLoc.x() - targetLoc.x());
        int dy = Math.abs(attackerLoc.y() - targetLoc.y());
        int distance = Math.max(dx, dy);

        // check range
        if (distance > RANGE) {
            return attacker + " is too far away to shoot " + target + ".";
        }

        // hit chance
        if (Math.random() * 100 >= HIT_RATE) {
            return attacker + " misses " + target + " with an arrow.";
        }

        // apply base damage
        target.hurt(DAMAGE);

        // trigger coating effect (if any)
        triggerCoating(attacker, target, map);

        // final message
        return String.format("%s shoots %s for %d damage", attacker, target, DAMAGE);
    }

    @Override
    public String toString() {
        return super.toString(); // include coating name if present
    }
}
