package game.actors;

import game.Ability;
import game.Species;
import game.actors.attributes.AnimalAttribute;
import game.weapons.CrocBite;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class CrocodileTest {

    @Test
    void hasExpectedAbilities() {
        Crocodile c = new Crocodile("Croc", '<', 300);
        assertTrue(c.hasAbility(Ability.CAN_ATTACK), "Crocodile should be able to attack");
        assertTrue(c.hasAbility(Species.CROCODILE), "Crocodile species ability should be set");
    }

    @Test
    void startsWithWarmth55() {
        Crocodile c = new Crocodile("Croc", '<', 300);
        int warmth = c.getAttribute(AnimalAttribute.WARMTH);
        assertEquals(55, warmth, "Crocodile should start with warmth = 55");
    }

    @Test
    void intrinsicWeaponIsCrocBiteWithExpectedStats() throws Exception {
        Crocodile c = new Crocodile("Croc", '<', 300);
        var w = c.getIntrinsicWeapon(); // from engine Actor API

        // 1) exact class check (no instanceof)
        assertEquals(CrocBite.class, w.getClass(), "Intrinsic weapon must be CrocBite");

        // 2) verify internal fields via reflection (engine has no getters)
        Class<?> clazz = w.getClass().getSuperclass(); // IntrinsicWeapon
        assertNotNull(clazz, "CrocBite should extend IntrinsicWeapon");

        Field damage = clazz.getDeclaredField("damage");
        Field verb = clazz.getDeclaredField("verb");
        Field hitRate = clazz.getDeclaredField("hitRate");
        Field name = clazz.getDeclaredField("name");
        damage.setAccessible(true);
        verb.setAccessible(true);
        hitRate.setAccessible(true);
        name.setAccessible(true);

        assertEquals(80, damage.getInt(w), "Croc bite damage should be 80");
        assertEquals("bites", verb.get(w), "Croc bite verb should be 'bites'");
        assertEquals(75, hitRate.getInt(w), "Croc bite hit rate should be 75%");
        assertEquals("Crocbite", name.get(w), "Croc bite name should be 'Crocbite'");
    }

    @Test
    void tameByIsNoOpAndReturnsEmptyString() {
        Crocodile c = new Crocodile("Croc", '<', 300);
        String result = c.tameBy(null, null); // method returns "" in your implementation
        assertEquals("", result, "tameBy should return empty string");
        assertFalse(c.hasAbility(Ability.TAMED), "Crocodile should not become TAMED");
    }
}
