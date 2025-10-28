package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.positions.teleport.TeleDoor;
import game.positions.teleport.TeleportDestination;
import game.positions.teleport.TeleportationCircle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Teleport to a chosen destination. Used by TeleDoor, TeleportationCircle, and TeleportCube.
 * @author Daffa Arrazy
 */
public class TeleportAction extends Action {
    private final List<TeleportDestination> destinations;
    private final boolean burnAroundSource;
    private final Integer forcedIndex;     // which destination to use
    private final boolean cube50Malfunction;
    private final String menuLabel;        // shown in the UI

    private static final Random RNG = new Random();

    /** For door/circle when you want a specific destination and a readable label. */
    public TeleportAction(List<TeleportDestination> destinations,
                          boolean burnAroundSource,
                          int forcedIndex,
                          boolean cube50Malfunction,
                          String menuLabel) {
        this.destinations = destinations;
        this.burnAroundSource = burnAroundSource;
        this.forcedIndex = forcedIndex;
        this.cube50Malfunction = cube50Malfunction;
        this.menuLabel = (menuLabel == null ? "Teleport" : menuLabel);
    }

    /** Legacy ctor (defaults to first destination + generic label). */
    public TeleportAction(List<TeleportDestination> destinations, boolean burnAroundSource) {
        this(destinations, burnAroundSource, 0, false, "Teleport");
    }

    @Override
    public String execute(Actor actor, GameMap currentMap) {
        Location src = currentMap.locationOf(actor);

        // Decide outcome first if this is a cube use
        boolean malfunction = cube50Malfunction && new Random().nextBoolean();

        TeleportDestination targetDest;
        if (malfunction) {
            // 50% malfunction → random free tile on the CURRENT map
            Location random = randomFreeLocation(currentMap, actor);
            if (random == null) return actor + " tried to teleport, but couldn't find a safe spot!";
            // Skip burns entirely for cube uses (spec)
            currentMap.moveActor(actor, random);
            return actor + " teleported to (" + random.x() + "," + random.y() + ")";
        }

        // Success path:
        // If forcedIndex >= 0, use it; otherwise pick the first destination that is on a DIFFERENT map
        int idx;
        if (forcedIndex != null && forcedIndex >= 0 && forcedIndex < destinations.size()) {
            idx = forcedIndex;
        } else {
            // Pick "other map" index if available; fallback to 0
            int otherIdx = -1;
            for (int i = 0; i < destinations.size(); i++) {
                if (destinations.get(i).map != currentMap) { otherIdx = i; break; }
            }
            idx = (otherIdx >= 0) ? otherIdx : 0;
        }
        targetDest = destinations.get(idx);

        // Find a safe landing tile (dest or an adjacent free tile)
        Location intended = targetDest.toLocation();
        Location targetLoc = safeDestination(intended, actor);
        if (targetLoc == null) return actor + " tried to teleport, but the destination is blocked!";

        // Burn effects:
        // - Doors/circles burn; cube does NOT (spec). We’re in success path and cube50Malfunction may be true,
        //   but since malfunction=false here, we check the flag to skip burn for cube-origin calls.
        if (!cube50Malfunction) {
            if (burnAroundSource) {
                TeleportationCircle.burnOneAroundSource(src, new Random());
            } else {
                TeleDoor.burnAroundDestination(new TeleportDestination(targetLoc.map(), targetLoc.x(), targetLoc.y()));
            }
        }

        // Perform the teleport
        currentMap.moveActor(actor, targetLoc);
        return actor + " teleported to (" + targetLoc.x() + "," + targetLoc.y() + ")";
    }

    @Override
    public String menuDescription(Actor actor) {
        return menuLabel;
    }

    // ----------------- helpers -----------------

    /** If dest is blocked, return a free adjacent location; otherwise return dest. Null if none found. */
    private static Location safeDestination(Location dest, Actor actor) {
        if (canStandOn(dest, actor)) return dest;

        // Try immediate neighbors
        List<Location> candidates = new ArrayList<>();
        for (var exit : dest.getExits()) {
            Location n = exit.getDestination();
            if (canStandOn(n, actor)) candidates.add(n);
        }
        if (!candidates.isEmpty()) {
            return candidates.get(RNG.nextInt(candidates.size()));
        }
        return null; // completely blocked
    }

    /** Pick a random free, enterable tile on a map (limited attempts). */
    private static Location randomFreeLocation(GameMap map, Actor actor) {
        int w = map.getXRange().max() + 1;
        int h = map.getYRange().max() + 1;

        for (int tries = 0; tries < 300; tries++) {
            int x = RNG.nextInt(Math.max(1, w));
            int y = RNG.nextInt(Math.max(1, h));
            Location loc = map.at(x, y);
            if (canStandOn(loc, actor)) return loc;
        }
        return null; // unlikely unless map is packed
    }

    /** True if location is empty and the ground allows entry. */
    private static boolean canStandOn(Location loc, Actor actor) {
        return loc != null
                && loc.getActor() == null
                && loc.canActorEnter(actor);
    }
}
