package game.system;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.positions.Dirt;

import java.util.*;

/**
 * Global system that manages fire tiles created by Torch.
 * Fire damages actors standing on it each turn and disappears after duration.
 */
public final class FireSystem {
    private FireSystem() {}

    /** key: tile Location; value: Fire data */
    private static final Map<Location, FireCell> FIRE_MAP = new HashMap<>();

    /** represent a burning tile */
    private static class FireCell {
        int remainingTurns;
        int damagePerTurn;

        FireCell(int turns, int damage) {
            this.remainingTurns = turns;
            this.damagePerTurn = damage;
        }
    }

    /** spawn fire around attacker */
    public static void spawnAround(Actor attacker, GameMap map, int radius, int turns, int dpt) {
        Location center = map.locationOf(attacker);

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                if (dx == 0 && dy == 0) continue; // skip center
                int newX = center.x() + dx;
                int newY = center.y() + dy;
                if (!map.getXRange().contains(newX) || !map.getYRange().contains(newY)) continue;

                Location loc = map.at(newX, newY);
                // Old fire covering the same tile
                FIRE_MAP.put(loc, new FireCell(turns, dpt));
            }
        }
    }

    /** Update the flame status every round */
    public static void tick(GameMap map) {
        List<Location> toRemove = new ArrayList<>();

        for (Map.Entry<Location, FireCell> entry : FIRE_MAP.entrySet()) {
            Location loc = entry.getKey();
            FireCell fire = entry.getValue();

            // If a character stands on fire, loses blood
            if (map.isAnActorAt(loc)) {
                Actor a = map.getActorAt(loc);
                a.hurt(fire.damagePerTurn);
            }

            fire.remainingTurns--;
            if (fire.remainingTurns <= 0) {
                toRemove.add(loc);
            }
        }

        // Removed the extinguished fire and changed the ground to Dirt
        for (Location loc : toRemove) {
            FIRE_MAP.remove(loc);
            loc.setGround(new Dirt());
        }
    }
}
