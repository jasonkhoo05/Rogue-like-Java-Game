package game;

import edu.monash.fit2099.engine.actors.Actor;
import game.actions.AttackAction;

import java.util.HashMap;
import java.util.Map;

public class AttackTracker {
    private static final Map<Actor, AttackAction> lastAttacks = new HashMap<>();

    /**
     * Records who the attacker's targeted
     * @param attacker the attacker
     * @param action the AttackAction
     */
    public static void recordAttack(Actor attacker, AttackAction action) {
        lastAttacks.put(attacker, action);
    }

    /**
     * Retrieves the AttackAction that the attacker last attacked with
     * @param attacker the attacker
     * @return the AttackAction that the attacker last attacked with
     */
    public static AttackAction getLastAttack(Actor attacker) {
        return lastAttacks.get(attacker);
    }
}
