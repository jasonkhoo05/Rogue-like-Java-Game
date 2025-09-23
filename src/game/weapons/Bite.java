package game.weapons;

import edu.monash.fit2099.engine.weapons.IntrinsicWeapon;

/**
 * Class representing an intrinsic weapon called a bite.
 * This intrinsic weapon deals 50 damage points with a 50% chance
 * to hit the target.
 */
public class Bite extends IntrinsicWeapon {
    public Bite() {
        super(50, "bites", 50, "jaws");
    }
}
