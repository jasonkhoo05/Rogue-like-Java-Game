package game.spawning;

public interface SpawnPolicy {
    boolean shouldSpawn();
    void onTick();  // advance any internal counters if needed
}
