package game.magicalOrbs;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.ActivateOrbAction;
import game.items.magicalOrbs.SpeedOrb;
import game.status.SpeedBoost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class SpeedOrbTest {

    private SpeedOrb speedOrb;
    private Actor mockActor;
    private GameMap mockMap;

    @BeforeEach
    void setUp() {
        speedOrb = new SpeedOrb();
        mockActor = mock(Actor.class);
        mockMap = mock(GameMap.class);
        Location mockLocation = mock(Location.class);
        when(mockMap.locationOf(mockActor)).thenReturn(mockLocation);
    }

    @Test
    void testApplyEffectAddsSpeedBoost() {
        // When
        speedOrb.applyEffect(mockActor);

        // Then — use a lambda matcher to avoid generics issues
        verify(mockActor).addStatus(argThat(argument -> {
            if (!(argument instanceof SpeedBoost sb)) return false;
            try {
                var field = SpeedBoost.class.getDeclaredField("duration");
                field.setAccessible(true);
                int duration = (int) field.get(sb);
                return duration == 5;
            } catch (Exception e) {
                return false;
            }
        }));
    }

    @Test
    void testApplyEffectReturnsCorrectMessage() {
        String result = speedOrb.applyEffect(mockActor);
        assertEquals("speed boost effect has been applied.", result);
    }

    @Test
    void testAllowableActionsCreatesActionListWithActivateOrbAction() {

        // When
        ActionList actions = speedOrb.allowableActions(mockActor, mockMap);

        // Then
        assertNotNull(actions, "ActionList should not be null");

        Action first = actions.get(0);
        assertInstanceOf(ActivateOrbAction.class, first, "First action should be an instance of ActivateOrbAction");
    }

    @Test
    void testGetExtraMoveActionsIsCalledWhenActorHasSpeedBoost() {
        // Given the actor already has a SpeedBoost
        when(mockActor.hasStatus(SpeedBoost.class)).thenReturn(true);

        // When
        ActionList actions = speedOrb.allowableActions(mockActor, mockMap);

        // Then
        assertEquals(actions.get(0).menuDescription(mockActor), mockActor.toString() + " activates Speed Orb");
    }
}