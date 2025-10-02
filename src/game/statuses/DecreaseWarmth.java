// file: game/statuses/DecreaseWarmth.java
package game.statuses;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperation;
import edu.monash.fit2099.engine.actors.attributes.BaseAttributes;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.attributes.AnimalAttribute;

/** Decreases warmth by 1 each tick; when <= 0, mark unconscious via HEALTH=0. */
public class DecreaseWarmth implements Status {

    @Override
    public void tickStatus(GameEntity entity, Location location) {
        // Attached only to our animals, so direct cast is fine in this codebase
        Actor a = (Actor) entity;

        a.modifyAttribute(AnimalAttribute.WARMTH, ActorAttributeOperation.DECREASE, 1);

        if (a.getAttribute(AnimalAttribute.WARMTH) <= 0) {
            // Use engine unconscious flag: HEALTH = 0
            a.modifyAttribute(BaseAttributes.HEALTH, ActorAttributeOperation.UPDATE, 0);
        }
    }

    @Override
    public boolean isStatusActive() { return true; }

    @Override
    public String toString() { return "DecreaseWarmth"; }
}
