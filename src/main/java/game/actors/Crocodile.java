
package game.actors;


import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;

import game.Ability;
import game.Species;
import game.behaviours.HostileBehaviour;
import game.behaviours.WanderBehaviour;
import game.weapons.CrocBite;
/**
 * Crocodile ('<'):
 * - 300 HP
 * - Intrinsic bite: 80 dmg, 75% hit chance
 * - Warmth starts at 55; when it hits 0 the animal becomes unconscious (handled by Animal base + DecreaseWarmth).
 * - Behaviours: Hostile (attack adjacent enemies) and Wander.
 */
public class Crocodile extends Animal {
    /**
     * Construct a Crocodile.
     */
    public Crocodile(String name, char displayChar, int hitPoints) {
        super(name, displayChar, hitPoints, 55);
        // Intrinsic weapon
        this.setIntrinsicWeapon(new CrocBite());

        // Default behaviours
        this.behaviours.put(1, new HostileBehaviour()); // attack when possible
        this.behaviours.put(999, new WanderBehaviour()); // otherwise wander

        // Allow others to attack it / it to attack
        this.enableAbility(Ability.CAN_ATTACK);

        this.enableAbility(Species.CROCODILE);
    }

    @Override
    public String tameBy(Actor actor, Item item) {
        return "";
    }
}
