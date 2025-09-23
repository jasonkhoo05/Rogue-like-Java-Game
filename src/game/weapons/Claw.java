package game.weapons;

import edu.monash.fit2099.engine.weapons.IntrinsicWeapon;

/**
 * Class representing an intrinsic weapon called a claw.
 * This intrinsic weapon deals 75 damage points with a 80% chance
 * to hit the target.
 */
public class Claw extends IntrinsicWeapon {
    public Claw() {
        super(75, "claws", 80, "claw");
    }
}
