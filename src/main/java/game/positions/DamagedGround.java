package game.positions;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;

/** Permanently impassable damaged/rubble ground ('*'). */
public class DamagedGround extends Ground {

    public DamagedGround() {
        super('*', "Damaged Ground");
    }

    /** cannot entre */
    @Override
    public boolean canActorEnter(Actor actor) {
        return false;
    }


    @Override
    public void tick(Location location) {
        // no-op:
    }

    /** Provide a safe replacement tool: do not replace if someone is currently in the current cell,
     * to avoid "trapping" someone on inaccessible ground. */
    public static boolean replaceIfFree(Location loc) {
        if (!loc.containsAnActor()) {
            loc.setGround(new DamagedGround());
            return true;
        }
        return false;
    }
}
