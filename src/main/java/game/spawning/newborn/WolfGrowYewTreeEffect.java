package game.spawning.newborn;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.Species;
import game.positions.trees.ProximityYewBerryTree;


import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

/**
 * After a WOLF is spawned, grow ONE ProximityYewBerryTree on a random exit
 * of the spawner tile. That tree drops berries when actors are nearby.
 */
public class WolfGrowYewTreeEffect implements SpawnContextEffect {
    private final Random rng = new Random();
    private final Consumer<String> log;

    /** @param log use msg -> display.println(msg); if null, no-op */
    public WolfGrowYewTreeEffect(Consumer<String> log) {
        this.log = (log != null) ? log : s -> {};
    }

    @Override
    public void apply(Actor newborn, Location spawnerLocation) {
        // Only apply to wolves
        if (!newborn.hasAbility(Species.WOLF)) return;

        List<Exit> exits = spawnerLocation.getExits();
        if (exits.isEmpty()) {
            log.accept(String.format("[SPAWN] %s at (%d,%d): no exits — cannot grow proximity yew tree",
                    newborn, spawnerLocation.x(), spawnerLocation.y()));
            return;
        }

        Exit chosen = exits.get(rng.nextInt(exits.size()));
        Location dest = chosen.getDestination();

        // Replace ground at that exit with our special tree
        dest.setGround(new ProximityYewBerryTree());

        log.accept(String.format(
                "[SPAWN] %s at (%d,%d): grew proximity yew tree at (%d,%d) (exit: %s)",
                newborn, spawnerLocation.x(), spawnerLocation.y(), dest.x(), dest.y(), chosen.getName()
        ));
    }
}
