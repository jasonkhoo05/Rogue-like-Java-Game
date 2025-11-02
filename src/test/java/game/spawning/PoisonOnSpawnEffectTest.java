package game.spawning;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.BaseAttributes;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actors.Deer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for PoisonOnSpawnEffect.
 *
 * Covers: typical (animal poisoned), control (non-animal not poisoned),
 * and edge (zero turns).
 */
class PoisonOnSpawnEffectTest {

    /** Minimal non-animal actor (does NOT expose BehaviourHost). */
    static class DummyNonAnimal extends Actor {
        DummyNonAnimal(int hp) { super("Dummy", 'X', hp); }
        @Override public Action playTurn(ActionList a, Action l, GameMap m, Display d) { return new DoNothingAction(); }
        @Override public ActionList allowableActions(Actor o, String dir, GameMap m) { return new ActionList(); }
    }

    @Test
    void animalGetsPoisonAndTakesDamageEachTick_thenExpires() {
        // Arrange: Deer is an Animal -> exposes BehaviourHost
        Deer deer = new Deer("Deer", 'd', 100);
        PoisonOnSpawnEffect effect = new PoisonOnSpawnEffect(3, 5);

        int h0 = deer.getAttribute(BaseAttributes.HEALTH);

        // Act: applying effect (as if spawned), then tick statuses 3 times
        effect.apply(deer);
        deer.tickStatuses(null);
        int h1 = deer.getAttribute(BaseAttributes.HEALTH);
        deer.tickStatuses(null);
        int h2 = deer.getAttribute(BaseAttributes.HEALTH);
        deer.tickStatuses(null);
        int h3 = deer.getAttribute(BaseAttributes.HEALTH);

        // Assert: exactly 5 dmg per tick, 3 ticks total, then stops
        assertEquals(h0 - 5, h1);
        assertEquals(h0 - 10, h2);
        assertEquals(h0 - 15, h3);

        deer.tickStatuses(null); // poison should be expired now
        int h4 = deer.getAttribute(BaseAttributes.HEALTH);
        assertEquals(h3, h4, "No further damage after poison expires");
    }

    @Test
    void nonAnimalIsNotPoisoned_noDamageOverTicks() {
        DummyNonAnimal npc = new DummyNonAnimal(80);
        PoisonOnSpawnEffect effect = new PoisonOnSpawnEffect(3, 7);

        int h0 = npc.getAttribute(BaseAttributes.HEALTH);

        // Apply: should NO-OP because npc is not a BehaviourHost (not an Animal)
        effect.apply(npc);

        // Tick a few times: health should remain unchanged
        npc.tickStatuses(null);
        npc.tickStatuses(null);
        npc.tickStatuses(null);

        int hFinal = npc.getAttribute(BaseAttributes.HEALTH);
        assertEquals(h0, hFinal, "Non-animal must not be poisoned");
    }

    @Test
    void zeroTurnPoison_appliesButDealsNoDamage() {
        Deer deer = new Deer("Deer", 'd', 60);
        PoisonOnSpawnEffect effect = new PoisonOnSpawnEffect(0, 999);

        int h0 = deer.getAttribute(BaseAttributes.HEALTH);

        effect.apply(deer);
        // Even if applied, zero-turn poison should do nothing on ticks
        deer.tickStatuses(null);
        deer.tickStatuses(null);

        int hFinal = deer.getAttribute(BaseAttributes.HEALTH);
        assertEquals(h0, hFinal, "Zero-turn poison must cause no damage");
    }
}
