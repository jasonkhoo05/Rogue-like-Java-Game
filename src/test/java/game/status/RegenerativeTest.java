package game.status;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class RegenerativeTest {
    private GameEntity mockGameEntity;
    private Location mockLocation;
    Regenerative regenerative;

    @BeforeEach
    void setUp() {
        Actor mockActor = mock(Actor.class);
        mockGameEntity = mock(GameEntity.class);
        mockLocation = mock(Location.class);
        regenerative = new Regenerative(mockActor,5, 1);
    }

    @Test
    void testTickStatus() throws NoSuchFieldException, IllegalAccessException {
        regenerative.tickStatus(mockGameEntity, mockLocation);
        var field = HeatResistant.class.getDeclaredField("duration");
        field.setAccessible(true);
        int duration = (int) field.get(regenerative);
        assertEquals(4, duration);
    }

    @Test
    void testIsStatusActive() {
        boolean result = regenerative.isStatusActive();
        assertTrue(result);
    }
}