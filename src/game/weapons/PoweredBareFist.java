package game.weapons;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.Ability;

public class PoweredBareFist extends BareFist {

    public PoweredBareFist() {
        super();
    }

    @Override
    public String attack(Actor attacker, Actor target, GameMap map) {
        int totalDamage = super.damage; // use the base damage

        // Double damage if attacker has BOOST_DAMAGE ability
        if (attacker.hasAbility(Ability.BOOST_DAMAGE)) {
            totalDamage *= 2;
        }

        target.hurt(totalDamage);

        return String.format("%s %s %s for %d damage", attacker, super.verb, target, totalDamage);
    }
}
