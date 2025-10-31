package game.positions.spawners;

import edu.monash.fit2099.engine.GameEngineException;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.spawning.SpawnEffect;
import game.spawning.SpawnPolicy;
import game.spawning.newborn.SpawnContextEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/** Base ground that can spawn actors according to a policy (equal chance across factories). */
public abstract class SpawnerGround extends Ground {
    private final SpawnPolicy policy;
    private final List<Supplier<Actor>> factories = new ArrayList<>();

    /** Newborn-only effects (e.g., +HP, add behaviours). */
    private final List<SpawnEffect> newbornEffects = new ArrayList<>();
    /** Location-aware effects (need spawner Location). */
    private final List<SpawnContextEffect> contextEffects = new ArrayList<>();

    private final Random rng;

    protected SpawnerGround(char displayChar, String name, SpawnPolicy policy) {
        this(displayChar, name, policy, new Random());
    }

    protected SpawnerGround(char displayChar, String name, SpawnPolicy policy, Random rng) {
        super(displayChar, name);
        this.policy = policy;
        this.rng = rng;
    }

    public SpawnerGround addFactory(Supplier<Actor> factory) {
        if (factory != null) factories.add(factory); return this; }
    public SpawnerGround addEffect(SpawnEffect effect) { if (effect != null) newbornEffects.add(effect); return this; }
    public SpawnerGround addContextEffect(SpawnContextEffect effect) { if (effect != null) contextEffects.add(effect); return this; }


    /**
     * Hook: subclasses may block spawning this tick based on the Location context.
     * Default: allow (returns true).
     */
    protected boolean canAttemptSpawn(Location location) {
        return true;
    }

    @Override
    public void tick(Location location) {
        policy.onTick();
        if (!canAttemptSpawn(location)) return;   // <--- subclass gate
        if (location.containsAnActor()) return;
        if (factories.isEmpty()) return;
        if (!policy.shouldSpawn()) return;

        Actor newborn = factories.get(rng.nextInt(factories.size())).get();
        if (newborn == null) return;

        // Apply newborn-only effects before placement
        for (SpawnEffect e : newbornEffects) e.apply(newborn);

        try {
            location.addActor(newborn); // engine rule: one actor per tile

            // After successful placement, run location-aware effects
            for (SpawnContextEffect e : contextEffects) e.apply(newborn, location);
        } catch (GameEngineException | IllegalArgumentException ignored) {
            // Could not place this tick; skip quietly.
        }
    }
}
