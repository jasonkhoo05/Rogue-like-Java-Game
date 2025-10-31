package game.weapons;

import edu.monash.fit2099.engine.weapons.IntrinsicWeapon;
/**
 * Crocodile's bite: 80 damage, 75% hit rate.
 */
public class CrocBite extends IntrinsicWeapon {
    public CrocBite(){
        super(80, "bites", 75,"Crocbite"); //80 dmg , 75% hitrate
    }
}
