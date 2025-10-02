package game.spawning;

import java.util.Random;

public class ChanceEachTurnPolicy implements SpawnPolicy {
    private final double probability;
    private final Random rng = new Random();

    public ChanceEachTurnPolicy(double probability) { this.probability = probability; }

    @Override public boolean shouldSpawn() { return rng.nextDouble() < probability; }
    @Override public void onTick() { /* nothing */ }
}
