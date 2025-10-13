package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.weapons.Weapon;
import game.items.weapons.Bow;

/**
 * An action that performs a ranged attack up to 3 tiles away using a Bow.
 */
public class RangedAttackAction extends Action {
    private final Actor target;
    private final Weapon weapon;
    private final int range;

    public RangedAttackAction(Actor target, Weapon weapon, int range) {
        this.target = target;
        this.weapon = weapon;
        this.range  = range;
    }

    @Override
    public String execute(Actor actor, GameMap map) {
        var a = map.locationOf(actor);
        var t = map.locationOf(target);
        int dist = Math.max(Math.abs(a.x() - t.x()), Math.abs(a.y() - t.y()));
        if (dist > range) {
            return actor + " is too far away to shoot " + target + ".";
        }

        return weapon.attack(actor, target, map);
    }

    @Override
    public String menuDescription(Actor actor) {
        return actor + " shoots " + target + " (" + weapon +")";
    }
}
