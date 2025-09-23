package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.Behaviour;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.AttackTracker;
import game.actions.AttackAction;

public class FightAlongSideBehaviour implements Behaviour {
    private Actor master;

    /**
     * Constructor
     *
     * @param master the master to pass the item to
     */
    public FightAlongSideBehaviour(Actor master) {
        this.master = master;
    }

    /**
     * Generates the action to be performed by the actor
     *
     * @param animal the Actor acting
     * @param map the GameMap containing the Actor
     * @return the action to be performed by the actor
     */
    @Override
    public Action generateAction(Actor animal, GameMap map) {

        if (!map.contains(master)) {
            return null;
        }

        Location animalLocation = map.locationOf(animal);

        for (Location location : animalLocation.getNearbyLocations(2)) {
            if (location.containsAnActor()) {
                Actor other = location.getActor();

                AttackAction lastAttack = AttackTracker.getLastAttack(other);

                if (lastAttack != null && lastAttack.getTarget() == master) {
                    return new AttackAction(other, "near Player");
                }
            }
        }
        return null;
    }
}
