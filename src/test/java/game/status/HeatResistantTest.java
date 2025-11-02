package game.status;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.positions.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class HeatResistantTest {

    private GameEntity mockGameEntity;
    private Location mockLocation;
    HeatResistant heatResistant;

    @BeforeEach
    void setUp() {
        mockGameEntity = mock(GameEntity.class);
        mockLocation = mock(Location.class);
        heatResistant = new HeatResistant(5);
    }

    @Test
    void testTickStatus() throws NoSuchFieldException, IllegalAccessException {
        heatResistant.tickStatus(mockGameEntity, mockLocation);
            var field = HeatResistant.class.getDeclaredField("duration");
            field.setAccessible(true);
            int duration = (int) field.get(heatResistant);
            assertEquals(4, duration);
    }

    @Test
    void testIsStatusActive() {
        boolean result = heatResistant.isStatusActive();
        assertTrue(result);
    }
}