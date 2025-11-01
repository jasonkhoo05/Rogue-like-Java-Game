package game.actions;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.items.magicalOrbs.MagicalOrb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ActivateOrbActionTest {
    private Actor mockActor;
    private GameMap mockGameMap;
    private MagicalOrb mockMagicalOrb;
    private ActivateOrbAction activateOrbAction;

    @BeforeEach
    void setUp() {
        mockActor = mock(Actor.class);
        mockGameMap = mock(GameMap.class);
        mockMagicalOrb = mock(MagicalOrb.class);
        activateOrbAction = new ActivateOrbAction(mockMagicalOrb);
    }

    @Test
    void testExecute() {
        when(mockMagicalOrb.applyEffect(mockActor)).thenReturn("magical orb effect has been applied");
        String result = activateOrbAction.execute(mockActor, mockGameMap);
        verify(mockMagicalOrb).applyEffect(mockActor);
        assertEquals(mockActor + " used " + mockMagicalOrb + " and magical orb effect has been applied", result);
    }

    @Test
    void testMenuDescription() {
        String result = activateOrbAction.menuDescription(mockActor);
        assertEquals(mockActor + " activates " + mockMagicalOrb, result);
    }
}