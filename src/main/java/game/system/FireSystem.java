// game/system/FireSystem.java
package game.system;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.positions.Fire;

public final class FireSystem {
    private FireSystem() {}

    /** Change the ground to Fire(life) within a radius centered on the attacker. */
    public static void spawnAround(Actor attacker, GameMap map, int radius, int life) {
        Location center = map.locationOf(attacker);
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                if (dx == 0 && dy == 0) continue; //no fire at center
                int x = center.x() + dx, y = center.y() + dy;
                if (!map.getXRange().contains(x) || !map.getYRange().contains(y)) continue;

                map.at(x, y).setGround(new Fire(life));
            }
        }
    }
}
