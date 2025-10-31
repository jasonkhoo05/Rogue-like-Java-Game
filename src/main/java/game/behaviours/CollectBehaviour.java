package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.Behaviour;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.CollectAction;

public class CollectBehaviour implements Behaviour {
    private Actor master;

    /**
     * Constructor
     *
     * @param master the master to pass the item to
     */
    public CollectBehaviour(Actor master) {
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

        Location animalLocation = map.locationOf(animal);

        // collect item on the location of the animal first
        for (Item item : animalLocation.getItems()) {
            return new CollectAction(master, item, animalLocation);
        }

        // collect item in surroundings of animal
        for (Location location : animalLocation.getNearbyLocations(1)) {
            for (Item item : location.getItems()) {
                return new CollectAction(master, item, location);
            }
        }
        return null;
    }
}
