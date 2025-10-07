package game.items.weapons;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.weapons.Weapon;
import game.Ability;
import game.status.StatusEffects;

import java.util.Random;

/**
 * Axe (p) - A carryable weapon item.
 * Deals 15 damage with a 75% chance to hit.
 */
public class Axe extends Item implements Weapon {
    private static final int DAMAGE = 15;
    private static final int HIT_RATE = 75; // %
    private final Random rng = new Random();

    public Axe() {
        // name = "Axe", displayChar = 'p', portable = true
        super("Axe", 'p', true);
        this.enableAbility(Ability.WEAPON_ITEM);
    }

    @Override
    public String attack(Actor attacker, Actor target, GameMap map) {
        // check hit or not
        if (rng.nextInt(100) >= HIT_RATE) {
            return attacker + " misses " + target + ".";
        }
        // hit then reduce hp
        target.hurt(DAMAGE);
        if (rng.nextInt(100) >= 50) {
            StatusEffects.addBleed(target, 10, 2);
        }
        // test mode
        //StatusEffects.addBleed(target, 10, 2);
        return String.format("%s chops %s for %d damage", attacker, target, DAMAGE);
    }

    @Override
    public String toString() {
        return "Axe";
    }
}
