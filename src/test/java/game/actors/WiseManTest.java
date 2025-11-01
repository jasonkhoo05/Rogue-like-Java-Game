package game.actors;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actions.TalkAction;
import game.ai.Monologue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WiseManTest {

    private WiseMan wiseMan;
    private Monologue mockMonologue;

    @BeforeEach
    void setUp() throws Exception {
        wiseMan = new WiseMan("Wise Man", 'W', 100);
        mockMonologue = mock(Monologue.class);

        // Inject mock Monologue using reflection
        var field = WiseMan.class.getDeclaredField("monologueAI");
        field.setAccessible(true);
        field.set(wiseMan, mockMonologue);
    }

    @Test
    void testPerformMonologue_ReturnsExpectedText() {
        String prompt = "A wise thought about patience.";
        String expected = "Patience is the key to understanding.";
        when(mockMonologue.generate(prompt)).thenReturn(expected);

        String actual = wiseMan.performMonologue(prompt);
        assertEquals(expected, actual);
        verify(mockMonologue).generate(prompt);
    }

    @Test
    void testPerformMonologue_UsesDefaultPrompt_WhenContextIsNull() {
        String expected = "Default monologue text";
        when(mockMonologue.generate(anyString())).thenReturn(expected);

        String actual = wiseMan.performMonologue(null);
        assertEquals(expected, actual);
        verify(mockMonologue).generate(contains("wise man will say"));
    }

    @Test
    void testPlayTurn_ReturnsTalkActionEveryThirdTurn() {
        ActionList dummyActions = new ActionList();
        Display dummyDisplay = mock(Display.class);
        GameMap dummyMap = mock(GameMap.class);

        // Turn 1: should do nothing
        Action result1 = wiseMan.playTurn(dummyActions, null, dummyMap, dummyDisplay);
        assertInstanceOf(DoNothingAction.class, result1);

        // Turn 2: should do nothing
        Action result2 = wiseMan.playTurn(dummyActions, null, dummyMap, dummyDisplay);
        assertInstanceOf(DoNothingAction.class, result2);

        // Turn 3: should talk
        Action result3 = wiseMan.playTurn(dummyActions, null, dummyMap, dummyDisplay);
        assertInstanceOf(TalkAction.class, result3);
    }

    @Test
    void testToString_ReturnsWiseMan() {
        assertEquals("Wise Man", wiseMan.toString());
    }
}
