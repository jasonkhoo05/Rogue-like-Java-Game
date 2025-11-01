// game/status/HeatEmpowered.java
package game.status;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.Location;

/** Temporary status: Upon a successful attack, the holder will inflict Burn on the target. */
public class HeatEmpowered implements Status {
    private int duration;

    public HeatEmpowered(int turns) {
        this.duration = Math.max(1, turns);
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
