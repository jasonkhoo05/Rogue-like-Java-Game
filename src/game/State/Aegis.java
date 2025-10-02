package game.State;


import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.Ability;

public class Aegis implements Status {

    private int duration;

    /**
     * Constructor for Aegis state.
     * @param duration number of turns the state lasts
     */
    public Aegis(int duration) {
        this.duration = duration;
    }

    @Override
    public void tickStatus(GameEntity currEntity, Location location) {
        // Apply immunity only to the entity holding this status
        if (currEntity.hasAbility(Ability.HEALABLE)) {
            currEntity.enableAbility(Ability.IMMUNITY);  // mark as immune this turn
        }

        duration--;
        if (duration <= 0 && currEntity.hasAbility(Ability.IMMUNITY)) {
            // remove immunity once duration ends
            currEntity.disableAbility(Ability.IMMUNITY);
        }
    }

    @Override
    public boolean isStatusActive() {
        return duration > 0;
    }
}
