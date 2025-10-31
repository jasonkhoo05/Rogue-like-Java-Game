package game.spawning;

import java.util.Random;

public class ChanceEachTurnPolicy implements SpawnPolicy {
    private final double probability;
    private final Random rng;

    public ChanceEachTurnPolicy(double probability) {
        this(probability, new Random());
    }

    public ChanceEachTurnPolicy(double probability, Random rng) {
        this.probability = probability;
        this.rng = rng;
    }

    @Override
    public boolean shouldSpawn() {
        return rng.nextDouble() < probability;
    }

    @Override
    public void onTick() { /* no counter needed */ }
}
