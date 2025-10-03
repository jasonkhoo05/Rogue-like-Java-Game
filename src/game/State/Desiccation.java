package game.State;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.Location;
import game.capabilities.Dehydratable;

import java.util.Objects;

public class Desiccation implements Status {
    private int duration;

    /**
     * Constructor for Angry state.
     * @param duration number of turns the state lasts
     */
    public Desiccation(int duration) {
        this.duration = duration;
    }

    @Override
    public void tickStatus(GameEntity currEntity, Location location) {
        duration--;
        for (Location specificLocation : location.getNearbyLocations(3)) {
            if (specificLocation.getActorAs(Dehydratable.class) != null) {
                Objects.requireNonNull(specificLocation.getActorAs(Dehydratable.class)).dehydrate(3);
                break;
            }
        }
    }

    @Override
    public boolean isStatusActive() {
        return duration > 0;
    }
}
