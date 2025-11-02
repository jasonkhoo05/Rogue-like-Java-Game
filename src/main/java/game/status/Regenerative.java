package game.status;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.Location;

public class Regenerative implements Status {
    private final Actor target;
    private int duration;
    private final int healAmount;

    /**
     * Constructor
     * @param duration number of turns the status lasts
     */
    public Regenerative(Actor target, int duration, int healAmount) {
        this.target = target;
        this.duration = duration;
        this.healAmount = healAmount;
    }

    /**
     * Called once per tick to update the status of the current entity.
     *
     * @param currEntity the entity this status is attached to
     */
    @Override
    public void tickStatus(GameEntity currEntity, Location location) {
        target.heal(healAmount);
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
