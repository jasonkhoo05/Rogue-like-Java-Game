package game.items.weapons;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.weapons.Weapon;

/**
 * bow (c) - carryable Weapon item.
 * Deals 5 damage with 25% chance to hit.
 * Can only attack within 3 tiles range.
 */
public class bow extends Item implements Weapon {
    private static final int DAMAGE = 5;
    private static final int HIT_RATE = 25; // percent
    private static final int RANGE = 3;

    public bow() {
        // name, display char, portable
        super("Bow", 'c', true);
    }

    @Override
    public String attack(Actor attacker, Actor target, GameMap map) {
        // Range detection
        Location locA = map.locationOf(attacker);
        Location locT = map.locationOf(target);
        int dx = Math.abs(locA.x() - locT.x());
        int dy = Math.abs(locA.y() - locT.y());
        int distance = Math.max(dx, dy);  // Chebyshev distance, allowing eight directions

        if (distance > RANGE) {
            return attacker + " is too far away to shoot " + target + ".";
        }

        // Hit Detection
        if (Math.random() * 100 >= HIT_RATE) {
            return attacker + " misses " + target + " with an arrow.";
        }

        // create damage
        target.hurt(DAMAGE);
        return String.format("%s shoots %s for %d damage", attacker, target, DAMAGE);
    }

    @Override
    public String toString() {
        return "Bow";
    }
}
