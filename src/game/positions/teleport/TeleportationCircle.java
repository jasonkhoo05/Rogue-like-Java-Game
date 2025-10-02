package game.positions.teleport;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.TeleportAction;
import game.positions.Fire;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Teleportation circle (‘O’).
 * On use: randomly burns ONE neighboring tile of the SOURCE for 5 ticks, then becomes Dirt.
 * @author Daffa Arrazy
 */
public class TeleportationCircle extends Ground {
    private final List<TeleportDestination> destinations = new ArrayList<>();
    private final Random rng = new Random();

    public TeleportationCircle() {
        super('O', "TeleportationCircle");
    }

    public TeleportationCircle addDestination(TeleportDestination dest) {
        this.destinations.add(dest);
        return this;
    }

    @Override
    public ActionList allowableActions(Actor actor, Location location, String direction) {
        ActionList list = new ActionList();
        if (location.getActor() == actor && !destinations.isEmpty()) {
            for (int i = 0; i < destinations.size(); i++) {
                TeleportDestination d = destinations.get(i);
                String label = "Teleport via Circle to " + d.map.toString() + " (" + d.x + "," + d.y + ")";
                list.add(new TeleportAction(this.destinations, /*burnAroundSource*/ true,
                        i, /*cube50Malfunction*/ false, label));
            }
        }
        return list;
    }

    /** Burn exactly one random neighboring tile around the source for 5 ticks. */
    public static void burnOneAroundSource(Location source, Random rng) {
        var exits = source.getExits();
        if (exits.isEmpty()) return;
        var chosen = exits.get(rng.nextInt(exits.size())).getDestination();
        chosen.setGround(new Fire(5));
    }
}
