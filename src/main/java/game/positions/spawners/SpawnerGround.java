package game.positions.spawners;

import edu.monash.fit2099.engine.GameEngineException;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.actors.Actor;
import game.spawning.SpawnPolicy;
import game.spawning.SpawnEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/**
 * Base ground that can spawn actors according to a policy.
 * Equal chance across all registered factories.
 */
public abstract class SpawnerGround extends Ground {
    private final SpawnPolicy policy;
    private final List<Supplier<Actor>> factories = new ArrayList<>();
    private final List<SpawnEffect> effects = new ArrayList<>();
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
        if (factory != null) factories.add(factory);
        return this;
    }

    public SpawnerGround addEffect(SpawnEffect effect) {
        if (effect != null) effects.add(effect);
        return this;
    }

    @Override
    public void tick(Location location) {
        policy.onTick();

        if (location.containsAnActor()) return;
        if (factories.isEmpty()) return;
        if (!policy.shouldSpawn()) return;

        Actor newborn = factories.get(rng.nextInt(factories.size())).get();
        if (newborn == null) return;

        for (SpawnEffect e : effects) e.apply(newborn);

        // IMPORTANT: Catch engine's checked exception here.
        try {
            location.addActor(newborn);
        } catch (GameEngineException | IllegalArgumentException ex) {
            // Couldn’t place this tick (e.g., late occupancy). Skip quietly or log if desired.
            // System.err.println("Spawn skipped at " + location + ": " + ex.getMessage());
        }
    }
}
