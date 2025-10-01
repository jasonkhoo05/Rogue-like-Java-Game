package game.State;


import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.Ability;

public class Radiant implements Status {

    private int duration;

    /**
     * Constructor for Radiant state.
     * @param duration number of turns the state lasts
     */
    public Radiant(int duration) {
        this.duration = duration;
    }
    @Override
    public void tickStatus(GameEntity currEntity, Location location) {
        // Heal any adjacent entities with HEALABLE ability
        for (Exit exit : location.getExits()) {
            Location dest = exit.getDestination();
            if (dest.containsAnActor()) {
                GameEntity neighbour = dest.getActor();

                if (neighbour.hasAbility(Ability.HEALABLE)) {
                    neighbour.enableAbility(Ability.RECEIVED_HEAL);
                }
            }
        }
        duration--;
    }

    @Override
    public boolean isStatusActive() {
        return duration > 0;
    }
}