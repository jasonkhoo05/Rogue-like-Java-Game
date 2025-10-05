package game.items.weapons;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.StatusEffects;

import java.util.Random;

/**
 * Axe (p) - a carryable Weapon item.
 * Deals 15 damage with a 75% hit rate and may cause bleeding.
 * Can be coated with additional effects (e.g., snow, yewberry).
 */
public class Axe extends AbstractCoatableWeapon {
    private static final int DAMAGE = 15;
    private static final int HIT_RATE = 75;
    private final Random rng = new Random();

    public Axe() {
        super("Axe", 'p');
    }

    @Override
    public String attack(Actor attacker, Actor target, GameMap map) {
        // Hit Registration
        if (rng.nextInt(100) >= HIT_RATE) {
            return attacker + " misses " + target + ".";
        }

        // Hit causes basic damage
        target.hurt(DAMAGE);

        // 50% chance of bleeding
        if (rng.nextInt(100) < 50) {
            StatusEffects.addBleed(target, 10, 2);
        }

        // If coated, trigger the coating effect
        triggerCoating(attacker, target, map);

        // Output result description
        return String.format("%s chops %s for %d damage", attacker, target, DAMAGE);
    }

    @Override
    public String toString() {
        return super.toString(); // Keep the coating display logic of AbstractCoatableWeapon
    }
}
