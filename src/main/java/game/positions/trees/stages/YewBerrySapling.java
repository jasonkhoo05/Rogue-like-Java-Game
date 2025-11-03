package game.positions.trees.stages;

import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.items.YewBerry;
import game.positions.trees.Tree;
import game.positions.trees.YewBerryTree;

import java.util.List;
import java.util.Random;


/**
 * Represents the sapling stage of a Yew Berry Tree.
 * Display Character: 'b'
 * Behaviour:
 * - On Plains maps: produces fruit every 2 turns
 * - On Forest maps: does not produce fruit
 * - Every 3 turns, has a 50% chance to grow into YewBerryTree
 * - Blocks movement and thrown objects
 * Author: Daffa Arrazy
 */
public class YewBerrySapling extends Tree {
    private static final char DISPLAY = 'b';
    private static final String NAME = "Yew Berry Sapling";
    private static final int CHECK_EVERY = 3;
    private static final double GROW_PROB = 0.5;
    private static final int PLAINS_FRUIT_EVERY = 2;

    private final Random rng;

    /**
     * Default constructor (production use) — uses a real Random for natural randomness.
     */
    public YewBerrySapling() {
        this(new Random());
    }

    /**
     * Deterministic constructor (testing use) — inject your own Random.
     */
    public YewBerrySapling(Random rng) {
        super(DISPLAY, NAME);
        this.rng = rng;
    }

    /**
     * Handles fruiting and potential growth transition.
     * @param location tile where this sapling exists
     */

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
            if (decideToGrow()) {
                location.setGround(new YewBerryTree());
            }
        }
    }

    /**
     * Determines whether this sapling should mature.
     * @return true if growth occurs this cycle
     */
    protected boolean decideToGrow() {
        return rng.nextDouble() < GROW_PROB;
    }

    /**
     * Spawns a Yew Berry fruit in a random adjacent tile.
     */
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
