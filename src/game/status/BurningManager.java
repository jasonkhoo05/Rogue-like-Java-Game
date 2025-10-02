package game.status;

import edu.monash.fit2099.engine.actors.Actor;

import java.util.*;

/**
 * Global per-turn manager for stacking burning on actors.
 * - Each stack deals 'damage' per turn for 'ticksRemaining' turns.
 * - Stacks add up (e.g., two stacks of 5 dmg => 10 per turn).
 *
 * @author Daffa Arrazy
 */
public final class BurningManager {
    private static final class Stack {
        int damage;
        int ticksRemaining;
        Stack(int damage, int ticksRemaining) { this.damage = damage; this.ticksRemaining = ticksRemaining; }
    }

    private static final Map<Actor, List<Stack>> stacks = new HashMap<>();

    private BurningManager() {}

    /** Add a burning stack to an actor (stacking). */
    public static void addBurn(Actor actor, int damagePerTurn, int durationTurns) {
        stacks.computeIfAbsent(actor, k -> new ArrayList<>()).add(new Stack(damagePerTurn, durationTurns));
    }

    /** Apply all burning at the start of a turn, cleaning up expired stacks. */
    public static void tickAll(Iterable<? extends Actor> actorsIterable) {
        // Build a set of the actors currently present
        java.util.HashSet<Actor> present = new java.util.HashSet<>();
        if (actorsIterable != null) {
            for (Actor a : actorsIterable) {
                if (a != null) present.add(a);
            }
        }

        // Keep stacks only for actors still present
        stacks.keySet().retainAll(present);

        for (Actor a : new java.util.ArrayList<>(stacks.keySet())) {
            var list = stacks.get(a);
            if (list == null || list.isEmpty()) continue;

            int totalDamage = 0;
            for (var s : list) totalDamage += s.damage;

            if (totalDamage > 0 && a.isConscious()) a.hurt(totalDamage);

            // decrement + purge
            list.removeIf(s -> (--s.ticksRemaining) <= 0);
            if (list.isEmpty()) stacks.remove(a);
        }
    }
}
