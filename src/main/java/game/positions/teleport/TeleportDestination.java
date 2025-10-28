package game.positions.teleport;

import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;

/** Simple value object for teleport targets.
 * @author Daffa Arrazy
 *
 */

public class TeleportDestination {
    public final GameMap map;
    public final int x;
    public final int y;

    public TeleportDestination(GameMap map, int x, int y) {
        this.map = map;
        this.x = x;
        this.y = y;
    }

    public Location toLocation() {
        return map.at(x, y);
    }

    @Override
    public String toString() {
        return map.toString() + " @(" + x + "," + y + ")";
    }
}
