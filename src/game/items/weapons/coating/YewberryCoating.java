package game.items.weapons.coating;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.status.StatusEffects;

/**
 * Coating made from Yewberry.
 * When the weapon hits, the target becomes poisoned for 5 turns,
 * taking 4 damage per turn.
 */
public class YewberryCoating implements Coating {

    @Override
    public String name() {
        return "Yewberry";
    }

    @Override
    public void onHit(Actor attacker, Actor target, GameMap map) {
        // Trigger poison effect (can be stacked)
        StatusEffects.addPoison(target, 4, 5);
    }
}
