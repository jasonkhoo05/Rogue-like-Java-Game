package game.positions.teleport;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.TeleportAction;
import game.positions.Fire;

import java.util.ArrayList;
import java.util.List;

/**
 * Tele-door (‘#’) — lets actors teleport intra/inter-map.
 * On use: the destination's surrounding tiles ignite (Fire ^) for 3 ticks and later become Dirt (+).
 * @author Daffa Arrazy
 */
public class TeleDoor extends Ground {
    private final List<TeleportDestination> destinations = new ArrayList<>();

    public TeleDoor() {
        super('#', "TeleDoor");
    }

    public TeleDoor addDestination(TeleportDestination dest) {
        this.destinations.add(dest);
        return this;
    }

    @Override
    public ActionList allowableActions(Actor actor, Location location, String direction) {
        ActionList list = new ActionList();
        if (location.getActor() == actor && !destinations.isEmpty()) {
            for (int i = 0; i < destinations.size(); i++) {
                TeleportDestination d = destinations.get(i);
                String label = "Teleport via Door to " + d.map.toString() + " (" + d.x + "," + d.y + ")";
                list.add(new TeleportAction(this.destinations, /*burnAroundSource*/ false,
                        i, /*cube50Malfunction*/ false, label));
            }
        }
        return list;
    }

    /** Ignite neighbors of the destination (3 ticks), turning into Dirt afterwards. */
    public static void burnAroundDestination(TeleportDestination dest) {
        var center = dest.toLocation();
        for (var exit : center.getExits()) {
            var loc = exit.getDestination();
            // Replace the ground with Fire (which later becomes Dirt)
            loc.setGround(new Fire(3));
        }
    }
}
