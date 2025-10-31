
package game.spawning;

import edu.monash.fit2099.engine.actors.Actor;
import game.capabilities.BehaviourHost;     // animals expose this as a capability
import game.status.Poisoned;               // <-- use the actual class name



/**
 * Apply a poison status to the newborn if (and only if) it is an animal.
 * We detect animals via the BehaviourHost capability (no instanceof).
 */
public class PoisonOnSpawnEffect implements SpawnEffect {
    private final int turns;
    private final int damagePerTurn;


    public PoisonOnSpawnEffect(int turns, int damagePerTurn) {
        this.turns = turns;
        this.damagePerTurn = damagePerTurn;

    }

    @Override
    public void apply(Actor newborn) {
        // Only animals (your codebase exposes BehaviourHost on Animal)
        if (newborn.asCapability(BehaviourHost.class).isEmpty()) return;

        newborn.addStatus(new Poisoned(turns, damagePerTurn));


    }
}
