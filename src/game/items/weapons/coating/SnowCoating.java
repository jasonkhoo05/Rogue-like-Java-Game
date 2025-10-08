package game.items.weapons.coating;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.status.StatusEffects;

/** Snow coating: on hit, apply frostbite (1 warmth/turn, 3 turns). */
public class SnowCoating implements Coating {
    @Override public String name() { return "Snow"; }

    @Override
    public void onHit(Actor attacker, Actor target, GameMap map) {
        // Can be stacked
        StatusEffects.addFrostbite(target, 1, 3);
    }
}

