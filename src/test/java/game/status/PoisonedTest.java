package game.status;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.BaseAttributes;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PoisonedTest {

    /** Minimal concrete actor for testing. */
    static class DummyActor extends Actor {
        DummyActor(int hp) {
            super("Dummy", 'D', hp);
        }
        @Override
        public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
            return new DoNothingAction();
        }
        @Override
        public ActionList allowableActions(Actor otherActor, String direction, GameMap map) {
            return new ActionList();
        }
    }

    /** Minimal concrete item (since engine Item is abstract). */
    static class DummyItem extends Item {
        DummyItem() {
            super("Rock", '*', true);
        }
    }

    @Test
    void dealsDamageEachTick_andExpiresAfterTurns() {
        DummyActor a = new DummyActor(100);
        Poisoned p = new Poisoned(3, 5);

        int h0 = a.getAttribute(BaseAttributes.HEALTH);
        p.tickStatus(a, null);
        int h1 = a.getAttribute(BaseAttributes.HEALTH);
        assertEquals(h0 - 5, h1);
        assertTrue(p.isStatusActive());

        p.tickStatus(a, null);
        int h2 = a.getAttribute(BaseAttributes.HEALTH);
        assertEquals(h0 - 10, h2);
        assertTrue(p.isStatusActive());

        p.tickStatus(a, null);
        int h3 = a.getAttribute(BaseAttributes.HEALTH);
        assertEquals(h0 - 15, h3);
        assertFalse(p.isStatusActive());

        p.tickStatus(a, null); // no more damage after expiry
        int h4 = a.getAttribute(BaseAttributes.HEALTH);
        assertEquals(h3, h4);
    }

    @Test
    void zeroTurns_noDamage_andInactive() {
        DummyActor a = new DummyActor(50);
        Poisoned p = new Poisoned(0, 999);

        assertFalse(p.isStatusActive());
        int before = a.getAttribute(BaseAttributes.HEALTH);
        p.tickStatus(a, null);
        int after = a.getAttribute(BaseAttributes.HEALTH);
        assertEquals(before, after);
    }

    @Test
    void nonActorEntities_areIgnored_andStatusNotDecremented() {
        Item rock = new DummyItem();
        Poisoned p = new Poisoned(2, 7);

        assertTrue(p.isStatusActive());

        p.tickStatus(rock, null); // no Actor capability → no decrement
        assertTrue(p.isStatusActive());

        p.tickStatus(rock, null);
        assertTrue(p.isStatusActive());
    }
}
