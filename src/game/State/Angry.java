package game.State;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.Location;

public class Angry implements Status {
    private int duration;

    /**
     * Constructor for Angry state.
     * @param duration number of turns the state lasts
     */
    public Angry(int duration) {
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
