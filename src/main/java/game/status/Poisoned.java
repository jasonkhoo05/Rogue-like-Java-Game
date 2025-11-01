package game.status;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperation;
import edu.monash.fit2099.engine.actors.attributes.BaseAttributes;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.Location;

/**
 * Deals damage each tick while active.
 * Uses capability lookup (no instanceof) to operate only on Actors.
 */
public class Poisoned implements Status {
    private int turns;
    private final int dmgPerTurn;

    public Poisoned(int turns, int dmgPerTurn) {
        this.turns = Math.max(0, turns);
        this.dmgPerTurn = dmgPerTurn;
    }

    @Override
    public void tickStatus(GameEntity currEntity, Location location) {
        if (!isStatusActive()) return;

        // Operate only if the entity exposes Actor as a capability (Actor is abstract -> allowed)
        currEntity.asCapability(Actor.class).ifPresent(a -> {
            a.modifyAttribute(BaseAttributes.HEALTH, ActorAttributeOperation.DECREASE, dmgPerTurn);
            turns--;
        });
    }

    @Override
    public boolean isStatusActive() {
        return turns > 0;
    }
}
