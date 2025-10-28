package game.capabilities;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;

/**
 * Represents tameable actors
 */
public interface Tameable {
    String tameBy(Actor actor, Item item);
}
