package game.items.weapons;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.weapons.Weapon;
import game.Ability;

/**
 * Bow (c) - a carryable ranged weapon item.
 * Deals 5 damage with 25% chance to hit.
 * Can attack targets within 3 tiles range.
 */
public class Bow extends Item implements Weapon {
    private static final int DAMAGE = 5;
    private static final int HIT_RATE = 25; // percent
    private static final int RANGE = 3;

    public Bow() {
        super("Bow", 'c', true);
        this.enableAbility(Ability.WEAPON_ITEM);
        this.enableAbility(Ability.RANGED_WEAPON);
    }

    @Override
    public String attack(Actor attacker, Actor target, GameMap map) {
        Location locA = map.locationOf(attacker);
        Location locT = map.locationOf(target);
        int dx = Math.abs(locA.x() - locT.x());
        int dy = Math.abs(locA.y() - locT.y());
        int distance = Math.max(dx, dy);


        if (distance > RANGE) {
            return attacker + " is too far away to shoot " + target + ".";
        }

        if (Math.random() * 100 < HIT_RATE) {
            target.hurt(DAMAGE);
            return String.format("%s shoots %s for %d damage.", attacker, target, DAMAGE);
        }

        return attacker + " misses the shot at " + target + ".";
    }


    public int getRange() { return RANGE; }

    @Override
    public String toString() {
        return "Bow";
    }
}
