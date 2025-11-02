package game.items;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actions.ReadJournalAction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class RecipeJournalTest {

    @Test
    void testAddAndRetrieveRecipes() {
        RecipeJournal journal = new RecipeJournal();

        journal.addRecipe("Berry Soup");
        journal.addRecipe("Spicy Carrot Stew");

        var recipes = journal.getRecipes();

        assertEquals(2, recipes.size());
        assertTrue(recipes.contains("Berry Soup"));
        assertTrue(recipes.contains("Spicy Carrot Stew"));

        // Ensure list returned is a copy (not the internal list)
        recipes.add("Should Not Affect Internal List");
        assertEquals(2, journal.getRecipes().size());
    }

    @Test
    void testHasRecipe() {
        RecipeJournal journal = new RecipeJournal();
        journal.addRecipe("Forest Salad");

        assertTrue(journal.hasRecipe("Forest Salad"));
        assertFalse(journal.hasRecipe("Stone Soup"));
    }

    @Test
    void testToStringFormat() {
        RecipeJournal journal = new RecipeJournal();
        journal.addRecipe("Berry Soup");
        journal.addRecipe("Honey Tea");

        String output = journal.toString();

        assertTrue(output.contains("=== Recipe Journal ==="));
        assertTrue(output.contains("- Berry Soup"));
        assertTrue(output.contains("- Honey Tea"));
    }
}