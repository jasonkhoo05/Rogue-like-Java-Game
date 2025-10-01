package game.State;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.Ability;
import game.actors.Player;

public class Vigor implements Status {

    private int duration;

    /**
     * Constructor for Radiant state.
     * @param duration number of turns the state lasts
     */
    public Vigor(int duration) {
        this.duration = duration;
    }

    @Override
    public void tickStatus(GameEntity currEntity, Location location) {
        if (duration > 0) {
            currEntity.enableAbility(Ability.BOOST_DAMAGE);
        } else {
            currEntity.disableAbility(Ability.BOOST_DAMAGE);
        }
        duration--;
    }

    @Override
    public boolean isStatusActive() {
        return duration > 0;
    }
}