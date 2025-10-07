package game.items;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.TeleportAction;
import game.positions.teleport.TeleportDestination;

import java.util.ArrayList;
import java.util.List;

/**
 * Teleport Cube (displayed as '0').
 * - Usable only when held.
 * - 50% chance to malfunction: teleports to a random tile on current map instead of intended destination.
 * @author Daffa Arrazy
 */
public class TeleportCube extends Item {
    private final List<TeleportDestination> destinations = new ArrayList<>();

    public TeleportCube() {
        super("Teleport Cube", '0', true);
    }

    public TeleportCube addDestination(TeleportDestination dest) {
        destinations.add(dest);
        return this;
    }

    // ---------- compat shims across engine variants (no @Override) ----------

    public ActionList allowableActions(Actor owner) {
        return actionsWhenHeld(owner, null);
    }

    public ActionList allowableActions(Actor owner, GameMap map) {
        return actionsWhenHeld(owner, map);
    }

    public ActionList getAllowableActions(Actor owner) {
        return actionsWhenHeld(owner, null);
    }

    public ActionList allowableActions(Location location) {
        return new ActionList(); // not usable from the ground
    }

    public ActionList getAllowableActions(Location location) {
        return new ActionList();
    }

    public ActionList allowableActions(Actor actor, Location location, String direction) {
        return new ActionList();
    }

    public ActionList getAllowableActions(Actor actor, Location location, String direction) {
        return new ActionList();
    }

    // ---------- internal helper ----------
    private ActionList actionsWhenHeld(Actor owner, GameMap map) {
        ActionList list = new ActionList();
        if (destinations.isEmpty()) return list;

        // Single entry; TeleportAction decides “other map” vs malfunction at runtime
        String label = "Use Teleport Cube";
        list.add(new game.actions.TeleportAction(
                destinations,
                /* burnAroundSource */ false,
                /* forcedIndex     */ -1,     // special: let action pick "other map"
                /* cubeMalfunction */ true,   // 50/50 malfunction handled in action
                /* menuLabel       */ label
        ));
        return list;
    }

}
