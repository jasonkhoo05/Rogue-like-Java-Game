
package game.positions.spawners;


import edu.monash.fit2099.engine.positions.Location;
import game.spawning.ChanceEachTurnPolicy;
import game.spawning.newborn.NearbyActorGatedChancePolicy;
import game.spawning.newborn.PoisonOnSpawnEffect;

/**
 * Swamp '~'
 * Spawns only if: there is at least one actor in surrounding tiles AND a 50% roll succeeds.
 * All newborn animals get Poisoned(10 turns @ 5 dmg/turn).
 * Prints simple messages to the provided logger (Display::println).
 */
public class Swamp extends SpawnerGround {
    public Swamp() {
        super('~', "Swamp", new ChanceEachTurnPolicy(0.5));

        // All animals spawned from swamps begin poisoned for 10 turns at 5 dmg/turn
        this.addEffect(new PoisonOnSpawnEffect(10, 5));
    }

    @Override
    protected boolean canAttemptSpawn(Location location) {
        // Need ANY actor in surrounding tiles (radius = 1)
        return location.getNearbyLocations(1).stream().anyMatch(Location::containsAnActor);
    }
}
