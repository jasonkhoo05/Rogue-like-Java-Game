package game.status;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.MoveActorAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SpeedBoostTest {
    private Actor mockActor;
    private Location mockHere;
    private Location mockNext;
    private Exit mockExit;
    private GameEntity mockGameEntity;
    private Location mockLocation;
    SpeedBoost speedBoost;

    @BeforeEach
    void setUp() {
        mockActor = mock(Actor.class);
        mockHere = mock(Location.class);
        mockNext = mock(Location.class);
        mockExit = mock(Exit.class);
        speedBoost = new SpeedBoost(5);
    }

    @Test
    void testGetExtraMoveActions_AddsRecursiveMoveActions() {
        // Arrange
        when(mockHere.getExits()).thenReturn(List.of(mockExit));
        when(mockExit.getDestination()).thenReturn(mockNext);
        when(mockExit.getName()).thenReturn("North");

        when(mockNext.canActorEnter(mockActor)).thenReturn(true);
        when(mockNext.getExits()).thenReturn(List.of(mockExit));

        // Act
        List<Action> actions = SpeedBoost.getExtraMoveActions(mockActor, mockHere);

        // Assert
        assertEquals(1, actions.size(), "Should generate one MoveActorAction");
        assertInstanceOf(MoveActorAction.class, actions.get(0));
        assertTrue(actions.get(0).menuDescription(mockActor).contains("North"),
                "Move description should include the direction 'North'");
    }

    @Test
    void testTickStatus() throws NoSuchFieldException, IllegalAccessException {
        speedBoost.tickStatus(mockGameEntity, mockLocation);
        var field = SpeedBoost.class.getDeclaredField("duration");
        field.setAccessible(true);
        int duration = (int) field.get(speedBoost);
        assertEquals(4, duration);
    }

    @Test
    void testIsStatusActive() {
        boolean result = speedBoost.isStatusActive();
        assertTrue(result);
    }
}