package game.status;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperation;
import edu.monash.fit2099.engine.actors.attributes.BaseAttributes;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.attributes.AnimalAttribute;
import game.actors.attributes.PlayerAttribute;

/**
 * Decrease animal warmth by 1 each tick.
 * When warmth reaches 0, set HEALTH=0 so the engine marks the actor unconscious.
 * Do NOT remove/print here — we delegate to playTurn + engine for printing/removal.
 */
public class DecreaseWarmth implements Status {
    private final Display display = new Display();
    @Override
    public void tickStatus(GameEntity entity, Location location) {
        // Control both animal and player's warmth here
        var maybeActor = entity.asCapability(Actor.class);
        if (maybeActor.isEmpty()) return;
        Actor a = maybeActor.get();

        Enum<?> warmthKey = null;
        if (a.hasStatistic(AnimalAttribute.WARMTH)) {
            warmthKey = AnimalAttribute.WARMTH;
        } else if (a.hasStatistic(PlayerAttribute.WARMTH)) {
            warmthKey = PlayerAttribute.WARMTH;
        }

        if (warmthKey == null) return;
        a.modifyAttribute(warmthKey, ActorAttributeOperation.DECREASE, 1);
        display.println(a + " feels cold!");

        if (a.getAttribute(warmthKey) <= 0) {
            a.modifyAttribute(BaseAttributes.HEALTH, ActorAttributeOperation.UPDATE, 0);
        }
    }

    @Override
    public boolean isStatusActive() {
        return true;
    }

    @Override
    public String toString() {
        return "DecreaseWarmth";
    }
}
