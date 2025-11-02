package game.status;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.Location;

public class HeatResistant implements Status {
    private int duration;

    /**
     * Constructor
     * @param duration number of turns the status lasts
     */
    public HeatResistant(int duration) {
        this.duration = duration;
    }

    /**
     * Called once per tick to update the status of the current entity.
     *
     * @param currEntity the entity this status is attached to
     */
    @Override
    public void tickStatus(GameEntity currEntity, Location location) {
        duration--;
    }

    /**
     * Indicates whether this status is still active.
     *
     * @return true if active, false otherwise
     */
    @Override
    public boolean isStatusActive() {
        return duration > 0;
    }
}
