package game;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;

import java.util.*;

/**
 * Global registry for Actor status effects (e.g., Bleed, Burn).
 * Each effect runs independently and can stack.
 */
public final class StatusEffects {
    private StatusEffects() {}

    private static final Map<Actor, List<Effect>> registry = new WeakHashMap<>();

    /** Add a bleeding effect */
    public static void addBleed(Actor target, int dpt, int turns) {
        registry.computeIfAbsent(target, k -> new ArrayList<>())
                .add(new BleedEffect(dpt, turns));
    }

    /** Add a burning effect */
    public static void addBurn(Actor target, int dpt, int turns) {
        registry.computeIfAbsent(target, k -> new ArrayList<>())
                .add(new BurnEffect(dpt, turns));
    }

    /** Add a poison effect */
    public static void addPoison(Actor target, int dpt, int turns) {
        registry.computeIfAbsent(target, k -> new ArrayList<>())
                .add(new PoisonEffect(dpt, turns));
    }

    /** Add a frostbite effect */
    public static void addFrostbite(Actor target, int warmthLoss, int turns) {
        registry.computeIfAbsent(target, k -> new ArrayList<>())
                .add(new FrostbiteEffect(warmthLoss, turns));
    }

    /** Called once per turn to process all active effects on this actor */
    public static void tick(Actor actor, GameMap map) {
        List<Effect> effects = registry.get(actor);
        if (effects == null) return;

        Iterator<Effect> it = effects.iterator();
        while (it.hasNext()) {
            Effect e = it.next();
            e.tick(actor, map);
            if (e.expired()) it.remove();
        }
    }

    // ========= Inner classes =========

    /** Basic interface for all effects */
    interface Effect {
        void tick(Actor actor, GameMap map);
        boolean expired();
    }

    /** Bleeding effect (e.g., Axe) */
    static class BleedEffect implements Effect {
        private int remaining;
        private final int damagePerTurn;

        BleedEffect(int dpt, int turns) {
            this.damagePerTurn = dpt;
            this.remaining = turns;
        }

        @Override
        public void tick(Actor actor, GameMap map) {
            actor.hurt(damagePerTurn);
            remaining--;
        }

        @Override
        public boolean expired() {
            return remaining <= 0;
        }
    }

    /** Burning effect (e.g., Torch) */
    static class BurnEffect implements Effect {
        private int remaining;
        private final int damagePerTurn;

        BurnEffect(int dpt, int turns) {
            this.damagePerTurn = dpt;
            this.remaining = turns;
        }

        @Override
        public void tick(Actor actor, GameMap map) {
            actor.hurt(damagePerTurn);
            remaining--;
        }

        @Override
        public boolean expired() {
            return remaining <= 0;
        }
    }

    /** Poison: Each round deducts health dpt, a total of turns, supports stacking */
    static class PoisonEffect implements Effect {
        private int remaining;
        private final int damagePerTurn;
        PoisonEffect(int dpt, int turns) {
            this.damagePerTurn = dpt;
            this.remaining = turns;
        }
        @Override
        public void tick(Actor actor, GameMap map) {
            actor.hurt(damagePerTurn);
            remaining--;
        }
        @Override
        public boolean expired() { return remaining <= 0; }
    }

    /** Frostbite: Reduces the target's WARMTH each round (if the target has this attribute); supports stacking */
    static class FrostbiteEffect implements Effect {
        private int remaining;
        private final int warmthLoss;
        FrostbiteEffect(int warmthLoss, int turns) {
            this.warmthLoss = warmthLoss;
            this.remaining = turns;
        }
        @Override
        public void tick(Actor actor, GameMap map) {
            if (actor instanceof game.actors.Player) {
                actor.modifyAttribute(
                        game.actors.attributes.PlayerAttribute.WARMTH,
                        edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperation.DECREASE,
                        warmthLoss
                );
            } else {
                // others are animals
                actor.modifyAttribute(
                        game.actors.attributes.AnimalAttribute.WARMTH,
                        edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperation.DECREASE,
                        warmthLoss
                );
            }
            remaining--;
        }
        @Override
        public boolean expired() { return remaining <= 0; }
    }


}
