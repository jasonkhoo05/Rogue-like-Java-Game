package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.Behaviour;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.ConsumeAction;
import game.capabilities.Consumable;

/** If there is a consumable item on this tile, consume it. */
public class ForageBehaviour implements Behaviour {
    @Override
    public Action generateAction(Actor actor, GameMap map) {
        Location here = map.locationOf(actor);
        for (Item it : here.getItems()) {
            if (it.asCapability(Consumable.class).isPresent()) {
                return new ConsumeAction(it);
            }
        }
        return null;
    }
}
