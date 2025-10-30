package game.positions.trees;

import edu.monash.fit2099.engine.positions.Location;
import game.capabilities.ItemSpawnable;
import game.items.YewBerry;

/**
 * A mature Yew Berry Tree that drops a YewBerry if there is ANY actor
 * on its surrounding tiles (its exits). No periodic (every-5-turn) drop.
 */
public class ProximityYewBerryTree extends YewBerryTree {
    public ProximityYewBerryTree() {
        super();
    }


    /** Override the periodic drop to disable YewBerryTree's "every 5 turns" behaviour. */
    @Override
    public void spawn(Location location) {
        // no-op: suppress periodic drops from YewBerryTree
    }

    @Override
    public void tick(Location location) {
        // keep base status/counter handling from Tree via YewBerryTree.tick(),
        // but periodic spawn is neutralised by our spawn() override above
        super.tick(location);

        // Proximity-based drop: if any adjacent tile has an actor, drop ONE berry
        var exits = location.getExits();
        boolean nearbyActor = exits.stream().anyMatch(e -> e.getDestination().containsAnActor());
        if (!nearbyActor || exits.isEmpty()) return;

        var pick = exits.get(ItemSpawnable.random.nextInt(exits.size()));
        pick.getDestination().addItem(new YewBerry());


    }
}
