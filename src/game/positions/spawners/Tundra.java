package game.positions.spawners;

import game.spawning.ChanceEachTurnPolicy;
import game.spawning.HealthBonusEffect;

/**
 * Tundra ('_'): 5% spawn chance each tick.
 * Newborns gain +10 HP (cold-resistant).
 */
public class Tundra extends SpawnerGround {
    public Tundra() {
        super('_', "Tundra", new ChanceEachTurnPolicy(0.05));
        this.addEffect(new HealthBonusEffect(10)); // cold resistance bonus
    }
}
