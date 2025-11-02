package game.weapons;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class CrocBiteTest {

    @Test
    void isExactlyCrocBiteAndExtendsIntrinsicWeapon() {
        CrocBite bite = new CrocBite();
        // exact class (no instanceof)
        assertEquals(CrocBite.class, bite.getClass(), "Concrete type must be CrocBite");
        // parent class check via hierarchy (engine unchanged)
        Class<?> parent = bite.getClass().getSuperclass();
        assertNotNull(parent, "CrocBite should have a superclass");
        assertEquals("IntrinsicWeapon", parent.getSimpleName(),
                "CrocBite should extend IntrinsicWeapon");
    }

    @Test
    void constructorSetsExpectedStats() throws Exception {
        CrocBite bite = new CrocBite();

        Class<?> iw = bite.getClass().getSuperclass(); // IntrinsicWeapon
        Field damage = iw.getDeclaredField("damage");
        Field verb = iw.getDeclaredField("verb");
        Field hitRate = iw.getDeclaredField("hitRate");
        Field name = iw.getDeclaredField("name");
        damage.setAccessible(true);
        verb.setAccessible(true);
        hitRate.setAccessible(true);
        name.setAccessible(true);

        assertEquals(80, damage.getInt(bite), "Damage should be 80");
        assertEquals("bites", verb.get(bite), "Verb should be 'bites'");
        assertEquals(75, hitRate.getInt(bite), "Hit rate should be 75%");
        assertEquals("Crocbite", name.get(bite), "Name should be 'Crocbite'");
    }

    @Test
    void toStringReturnsWeaponName() {
        CrocBite bite = new CrocBite();
        assertEquals("Crocbite", bite.toString(),
                "toString() should return the intrinsic weapon name");
    }

    @Test
    void sanityBoundsAreValid() throws Exception {
        CrocBite bite = new CrocBite();

        Class<?> iw = bite.getClass().getSuperclass();
        Field damage = iw.getDeclaredField("damage");
        Field hitRate = iw.getDeclaredField("hitRate");
        damage.setAccessible(true);
        hitRate.setAccessible(true);

        int dmg = damage.getInt(bite);
        int hr = hitRate.getInt(bite);

        assertTrue(dmg > 0, "Damage should be positive");
        assertTrue(hr >= 0 && hr <= 100, "Hit rate should be within [0, 100]");
    }
}
