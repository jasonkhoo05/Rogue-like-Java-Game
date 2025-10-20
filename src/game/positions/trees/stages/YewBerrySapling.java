package game.positions.trees.stages;

import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.items.YewBerry;
import game.positions.trees.Tree;
import game.positions.trees.YewBerryTree;

import java.util.List;
import java.util.Random;

/**
 * Yew Berry Sapling ("b").
 * Any map: every 3 turns, 50% chance to grow to YewBerryTree.
 * Forest: no fruit at sapling stage.
 * Plains: fruit every 2 turns.
 */
public class YewBerrySapling extends Tree {
    private static final char DISPLAY = 'b';
    private static final String NAME = "Yew Berry Sapling";
    private static final int CHECK_EVERY = 3;
    private static final double GROW_PROB = 0.5;
    private static final int PLAINS_FRUIT_EVERY = 2;

    private final Random rng = new Random();

    public YewBerrySapling() {
        super(DISPLAY, NAME);
    }

    @Override
    public void tick(Location location) {
        super.tick(location);

        String mapName = location.map().toString();

        // Map-dependent fruiting
        if ("Plains".equalsIgnoreCase(mapName) && every(PLAINS_FRUIT_EVERY)) {
            spawnYewBerry(location);
        }
        // Forest: no fruit at sapling stage

        // Probabilistic growth every 3 turns
        if (every(CHECK_EVERY)) {
            if (rng.nextDouble() < GROW_PROB) {
                location.setGround(new YewBerryTree());
            }
        }
    }

    private void spawnYewBerry(Location location) {
        List<Exit> exits = location.getExits();
        if (exits.isEmpty()) return;
        Location dest = exits.get(rng.nextInt(exits.size())).getDestination();
        location.map().at(dest.x(), dest.y()).addItem(new YewBerry());
    }

    @Override
    public boolean blocksThrownObjects() {
        return true;
    }

    @Override
    public boolean canActorEnter(edu.monash.fit2099.engine.actors.Actor actor) {
        return false;
    }
}
