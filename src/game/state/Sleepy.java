package game.state;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.Location;

public class Sleepy implements Status {
    private int duration;

    /**
     * Constructor for Sleepy state.
     * @param duration number of turns the state lasts
     */
    public Sleepy(int duration) {
        this.duration = duration;
    }

    @Override
    public void tickStatus(GameEntity currEntity, Location location) {
        duration--;
    }

    @Override
    public boolean isStatusActive() {
        return duration > 0;
    }
}
