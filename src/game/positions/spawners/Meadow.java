package game.positions.spawners;

import game.spawning.ForagingEffect;
import game.spawning.GatedChanceEveryNPolicy;

/**
 * Meadow ('w'): every 7 turns, 50% chance to spawn.
 * Newborns can forage (consume consumables on their tile).
 */
public class Meadow extends SpawnerGround {
    public Meadow() {
        super('w', "Meadow", new GatedChanceEveryNPolicy(7, 0.5));
        this.addEffect(new ForagingEffect());
    }
}
