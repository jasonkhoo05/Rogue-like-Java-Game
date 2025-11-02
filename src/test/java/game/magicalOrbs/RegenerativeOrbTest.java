package game.magicalOrbs;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.ActivateOrbAction;
import game.items.magicalOrbs.RegenerativeOrb;
import game.status.Regenerative;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class RegenerativeOrbTest {
    private RegenerativeOrb regenerativeOrb;
    private Actor mockActor;
    private GameMap mockMap;

    @BeforeEach
    void setUp() {
        regenerativeOrb = new RegenerativeOrb();
        mockActor = mock(Actor.class);
        mockMap = mock(GameMap.class);
        Location mockLocation = mock(Location.class);
        when(mockMap.locationOf(mockActor)).thenReturn(mockLocation);
    }

    @Test
    void testApplyEffectAddsRegenerative() {
        // When
        regenerativeOrb.applyEffect(mockActor);

        // Then — use a lambda matcher to avoid generics issues
        verify(mockActor).addStatus(argThat(argument -> {
            if (!(argument instanceof Regenerative sb)) return false;
            try {
                var field = Regenerative.class.getDeclaredField("duration");
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
        String result = regenerativeOrb.applyEffect(mockActor);
        assertEquals("Regenerative effect has been applied.", result);
    }

    @Test
    void testAllowableActionsCreatesActionListWithActivateOrbAction() {

        // When
        ActionList actions = regenerativeOrb.allowableActions(mockActor, mockMap);

        // Then
        assertNotNull(actions, "ActionList should not be null");

        Action first = actions.get(0);
        assertInstanceOf(ActivateOrbAction.class, first, "First action should be an instance of ActivateOrbAction");
    }
}