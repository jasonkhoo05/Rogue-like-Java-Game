package game.statuses;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperation;
import edu.monash.fit2099.engine.actors.attributes.BaseAttributes;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.attributes.AnimalAttribute;

/**
 * Decrease animal warmth by 1 each tick.
 * When warmth reaches 0, set HEALTH=0 so the engine marks the actor unconscious.
 * Do NOT remove/print here — we delegate to playTurn + engine for printing/removal.
 */
public class DecreaseWarmth implements Status {
    @Override
    public void tickStatus(GameEntity entity, Location location) {
        // This status is attached only to Animals in our codebase.
        Actor a = (Actor) entity;

        if (!a.hasStatistic(AnimalAttribute.WARMTH)) return;
        a.modifyAttribute(AnimalAttribute.WARMTH, ActorAttributeOperation.DECREASE, 1);

        if (a.getAttribute(AnimalAttribute.WARMTH) <= 0) {
            a.modifyAttribute(BaseAttributes.HEALTH, ActorAttributeOperation.UPDATE, 0);
        }
    }

    @Override
    public boolean isStatusActive() { return true; }

    @Override
    public String toString() { return "DecreaseWarmth"; }
}
