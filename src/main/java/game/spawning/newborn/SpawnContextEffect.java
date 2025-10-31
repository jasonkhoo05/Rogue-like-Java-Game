
package game.spawning.newborn;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;

/**
 * Location-aware spawn effect. Called after the newborn is successfully placed.
 * Use this for side effects around the spawner tile (drop items, grow trees, AOE poison, etc.).
 */

public interface SpawnContextEffect {
    void apply(Actor newborn, Location spawnerLocation);
}
