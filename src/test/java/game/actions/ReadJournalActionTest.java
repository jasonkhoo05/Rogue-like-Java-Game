package game.actions;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.capabilities.HasRecipeJournal;
import game.capabilities.Talkable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import static org.junit.jupiter.api.Assertions.*;

class ReadJournalActionTest {


    @Test
    void testReadJournal_WithEmptyJournal_NoAIComment() {
        // Create fake actor with journal
        FakeActor actor = new FakeActor(true);
        GameMap map = mock(GameMap.class);

        ReadJournalAction action = new ReadJournalAction();
        String result = action.execute(actor, map);

        // Should only show journal content (no comment since no recipes)
        assertTrue(result.contains("=== Recipe Journal ==="));
        assertFalse(result.contains("says")); // TalkAction AI comment uses "says"
    }

    @Test
    void testReadJournal_WithRecipes_AICommentIncluded() {
        // Create fake actor with journal + one recipe
        FakeActor actor = new FakeActor(true);
        actor.getRecipeJournal().addRecipe("Berry Soup");

        GameMap map = mock(GameMap.class);

        // Spy the action so we can stub the AI part
        ReadJournalAction action = spy(new ReadJournalAction());

        // Mock the internal TalkAction execution to avoid calling real AI
        doReturn("FakeActor says: Nice soup!").when(action)
                .executeTalkAction(any(), any(), eq("Berry Soup"));

        String result = action.execute(actor, map);

        assertTrue(result.contains("Berry Soup"));          // Journal shows recipes
        assertTrue(result.contains("FakeActor says: Nice soup!")); // AI comment appears
    }


}