package game.positions.trees.stages;

import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.items.Apple;
import game.positions.trees.AppleTree;
import game.positions.trees.Tree;

import java.util.List;
import java.util.Random;

/**
 * Wild Apple Sapling ("t"), stage 2.
 * Fruits every 2 turns; after 5 turns -> AppleTree.
 */
public class AppleSapling extends Tree {
    private static final char DISPLAY = 't';
    private static final String NAME = "Wild Apple Sapling";
    private static final int FRUIT_EVERY = 2;
    private static final int GROW_AFTER = 5;

    private final Random rng = new Random();

    public AppleSapling() {
        super(DISPLAY, NAME);
    }

    @Override
    public void tick(Location location) {
        super.tick(location);

        // Fruit cadence (map-agnostic per spec for sapling)
        if (age() % FRUIT_EVERY == 0) {
            spawnApple(location);
        }

        // Growth to mature
        if (age() >= GROW_AFTER) {
            location.setGround(new AppleTree());
        }
    }

    private void spawnApple(Location location) {
        List<Exit> exits = location.getExits();
        if (exits.isEmpty()) return;
        Location dest = exits.get(rng.nextInt(exits.size())).getDestination();
        location.map().at(dest.x(), dest.y()).addItem(new Apple());
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
