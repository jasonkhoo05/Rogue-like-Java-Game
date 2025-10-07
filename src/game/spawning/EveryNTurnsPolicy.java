package game.spawning;

public class EveryNTurnsPolicy implements SpawnPolicy {
    private final int n;
    private int counter = 0;

    public EveryNTurnsPolicy(int n) {
        if (n <= 0) throw new IllegalArgumentException("n must be > 0");
        this.n = n;
    }

    @Override
    public boolean shouldSpawn() {
        return counter == 0; // spawn on tick when counter is 0
    }

    @Override
    public void onTick() {
        counter = (counter + 1) % n;
    }
}
