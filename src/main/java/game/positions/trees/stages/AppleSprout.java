package game.positions.trees.stages;

import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.items.Apple;
import game.positions.trees.AppleTree;
import game.positions.trees.Tree;

import java.util.List;
import java.util.Random;

/**
 * Represents the first growth stage of a Wild Apple Tree.
 *
 * Display Character: ','
 *
 * Behaviour:
 * - On Forest maps: does not produce fruit and becomes AppleSapling after 3 turns
 * - On Plains maps: produces fruit every turn and becomes AppleTree after 3 turns (skips sapling)
 * - Blocks movement and thrown objects
 *
 * Author: Daffa Arrazy
 */
public class AppleSprout extends Tree {
    private static final char DISPLAY = ',';
    private static final String NAME = "Wild Apple Sprout";
    private static final int GROW_AFTER = 3;

    private final Random rng = new Random();

    public AppleSprout() {
        super(DISPLAY, NAME);
    }

    /**
     * Applies map-specific fruiting rules and determines stage transition.
     * @param location map location of the sprout
     */

    @Override
    public void tick(Location location) {
        super.tick(location);

        String mapName = location.map().toString(); // GameMap.toString() returns name

        // Map-dependent fruiting
        if ("Plains".equalsIgnoreCase(mapName)) {
            spawnApple(location, /*everyTurn*/ true);
        } // Forest: no fruit at sprout stage

        // Growth after N turns
        if (age() >= GROW_AFTER) {
            if ("Plains".equalsIgnoreCase(mapName)) {
                // Skip sapling on Plains
                location.setGround(new AppleTree());
            } else {
                // Default / Forest: go to sapling
                location.setGround(new AppleSapling());
            }
        }
    }

    /**
     * Spawns an apple in a random adjacent tile.
     */

    private void spawnApple(Location location, boolean everyTurn) {
        if (!everyTurn) return;
        List<Exit> exits = location.getExits();
        if (exits.isEmpty()) return;
        Location dest = exits.get(rng.nextInt(exits.size())).getDestination();
        location.map().at(dest.x(), dest.y()).addItem(new Apple());
    }

    @Override
    public boolean blocksThrownObjects() {
        return true; // same as other trees
    }

    @Override
    public boolean canActorEnter(edu.monash.fit2099.engine.actors.Actor actor) {
        return false;
    }
}
