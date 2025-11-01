package game.status;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.Location;

/**
 * While active, the actor cannot perform movement-type actions.
 * Attack/Interact/Use are still allowed.
 */
public class MovementLocked implements Status {
    private int duration;

    public MovementLocked(int turns) { this.duration = turns; }

    @Override
    public void tickStatus(GameEntity entity, Location location) {
        if (duration > 0) duration--;
    }

    @Override
    public boolean isStatusActive() { return duration > 0; }
}
