package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.Behaviour;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;

import java.util.Random;

public class HostileBehaviour implements Behaviour {
    private final Random random = new Random();

    /**
     * Generates the action to be performed by the actor
     *
     * @param actor the Actor acting
     * @param map the GameMap containing the Actor
     * @return the action to be performed by the actor
     */
    @Override
    public Action generateAction(Actor actor, GameMap map) {
        ActionList actions = new ActionList();

        for (Exit exit : map.locationOf(actor).getExits()) {
            Location destination = exit.getDestination();
            if (destination.containsAnActor()) {
                Actor target = destination.getActor();
                actions.add(target.allowableActions(actor, destination.toString(), map));
            }
        }

        try {
            return actions.get(random.nextInt(actions.size()));
        } catch (Exception e) {
            return null;
        }
    }
}
