package game.capabilities;

import edu.monash.fit2099.engine.actors.Actor;

/**
 * Represents consumable items
 */
public interface Consumable {
    String consumedBy(Actor actor);
}
