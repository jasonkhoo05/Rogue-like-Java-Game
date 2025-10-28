package game.spawning;

import java.util.Random;

/** Triggers every N turns, and when it triggers there is a probability p to spawn. */
public class GatedChanceEveryNPolicy implements SpawnPolicy {
    private final int n;
    private final double p;
    private final Random rng;
    private int counter = 0;

    public GatedChanceEveryNPolicy(int n, double p) {
        this(n, p, new Random());
    }

    public GatedChanceEveryNPolicy(int n, double p, Random rng) {
        if (n <= 0) throw new IllegalArgumentException("n must be > 0");
        if (p < 0.0 || p > 1.0) throw new IllegalArgumentException("p must be in [0,1]");
        this.n = n;
        this.p = p;
        this.rng = rng;
    }

    @Override
    public boolean shouldSpawn() {
        return counter == 0 && rng.nextDouble() < p;
    }

    @Override
    public void onTick() {
        counter = (counter + 1) % n;
    }
}
