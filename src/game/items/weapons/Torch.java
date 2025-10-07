package game.items.weapons;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.weapons.Weapon;
import game.Ability;
import game.StatusEffects;
import game.system.FireSystem;

import java.util.Random;

/**
 * Torch (y) - carryable Weapon item.
 * Deals 10 damage with a 50% chance to hit.
 */
public class Torch extends Item implements Weapon {
    private static final int DAMAGE = 10;
    private static final int HIT_RATE = 50; // percent
    private final Random rng = new Random();

    public Torch() {
        // name, display char, portable
        super("Torch", 'y', true);
        this.enableAbility(Ability.WEAPON_ITEM);
    }

    @Override
    public String attack(Actor attacker, Actor target, GameMap map) {
        if (rng.nextInt(100) >= HIT_RATE) {
            return attacker + " misses " + target + " with a torch.";
        }
        target.hurt(DAMAGE);
        StatusEffects.addBurn(target, 3, 7);
        FireSystem.spawnAround(attacker, map, 1, 5, 5);
        return String.format("%s scorches %s for %d damage", attacker, target, DAMAGE);
    }

    @Override
    public String toString() {
        return "Torch";
    }
}
