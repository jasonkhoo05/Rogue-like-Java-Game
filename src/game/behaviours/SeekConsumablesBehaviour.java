package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.MoveActorAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.Behaviour;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.capabilities.Consumable;

import java.util.*;

/**
 * Move one step toward the nearest reachable tile (within radius) that
 * contains a Consumable item. Pairs with ForageBehaviour(-1) so eating on
 * the current tile happens before seeking.
 */
public class SeekConsumablesBehaviour implements Behaviour {
    private final int radius;

    public SeekConsumablesBehaviour(int radius) {
        if (radius < 1) throw new IllegalArgumentException("radius must be >= 1");
        this.radius = radius;
    }

    @Override
    public Action generateAction(Actor actor, GameMap map) {
        Location origin = map.locationOf(actor);

        // BFS queue and parent map for path reconstruction
        Queue<Location> q = new ArrayDeque<>();
        Map<Location, Location> parent = new HashMap<>();
        Set<Location> visited = new HashSet<>();

        q.add(origin);
        visited.add(origin);

        Location goal = null;

        while (!q.isEmpty()) {
            Location cur = q.remove();

            // Limit search by radius (Manhattan distance from origin)
            if (manhattan(origin, cur) > radius) continue;

            if (cur != origin && hasConsumable(cur)) {
                goal = cur;
                break;
            }

            for (Exit e : cur.getExits()) {
                Location nxt = e.getDestination();

                // Only explore tiles we could stand on (respect terrain + one-actor-per-tile)
                if (visited.contains(nxt)) continue;
                if (!nxt.canActorEnter(actor)) continue;

                visited.add(nxt);
                parent.put(nxt, cur);
                q.add(nxt);
            }
        }

        if (goal == null) return null; // no reachable food within radius

        // Reconstruct first step from origin toward goal
        Location step = goal;
        while (parent.containsKey(step) && parent.get(step) != origin) {
            step = parent.get(step);
        }
        // If the goal is adjacent, step will be that neighbour; else, it's the first hop.

        // Find the exit from origin that leads to 'step'
        for (Exit e : origin.getExits()) {
            if (e.getDestination().equals(step)) {
                return new MoveActorAction(e.getDestination(), e.getName(), e.getHotKey());
            }
        }
        return null; // should not happen, but guard anyway
    }

    private int manhattan(Location a, Location b) {
        return Math.abs(a.x() - b.x()) + Math.abs(a.y() - b.y());
    }

    private boolean hasConsumable(Location loc) {
        for (Item it : loc.getItems()) {
            if (it.asCapability(Consumable.class).isPresent()) return true;
        }
        return false;
    }
}
