package game.actions;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.Ability;
import game.ai.RecipeGenerator;
import game.capabilities.HasRecipeJournal;
import game.items.RecipeJournal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


public class ConsumeActionTest {


    private GameMap mockMap;
    private RecipeGenerator mockRecipeGenerator;
    private TestItem testItem;
    private Location mockLocation;

    @BeforeEach
    void setUp() {
        mockMap = mock(GameMap.class);
        mockRecipeGenerator = mock(RecipeGenerator.class);

        // Create a real TestItem that implements Consumable
        testItem = new TestItem("TestBerry", "Item consumed.");
        // mock location to avoid NPE

        mockLocation = mock(Location.class);
        when(mockMap.locationOf(any(Actor.class))).thenReturn(mockLocation);
        when(mockLocation.getItems()).thenReturn(new java.util.ArrayList<>()); // safe empty list
    }


    @Test
    void testRecipeGeneratedWhenActorIsPlayer() {
        // Fake player actor
        FakeActor player = new FakeActor(true);

        // Stub recipe generation
        when(mockRecipeGenerator.generateRecipe("TestBerry")).thenReturn("MockRecipe: Berry Soup");

        // Create ConsumeAction with mocked generator
        ConsumeAction action = new ConsumeAction(testItem, mockRecipeGenerator);

        // Execute action
        String result = action.execute(player, mockMap);

        // Verify recipe generator called
        verify(mockRecipeGenerator, times(1)).generateRecipe("TestBerry");

        // Verify recipe is in the journal
        RecipeJournal journal = player.getRecipeJournal();
        assertTrue(journal.hasRecipe("MockRecipe: Berry Soup"));

        // Verify returned message contains both parts
        assertTrue(result.contains("Item consumed."));
        assertTrue(result.contains("discovers a new recipe: MockRecipe: Berry Soup"));
    }


    @Test
    void testRecipeNotGeneratedWhenActorIsNotPlayer() {
        FakeActor npc = new FakeActor(false);

        ConsumeAction action = new ConsumeAction(testItem, mockRecipeGenerator);
        String result = action.execute(npc, mockMap);

        // Verify generator never called
        verify(mockRecipeGenerator, never()).generateRecipe(anyString());

        // Journal should be empty
        assertTrue(npc.getRecipeJournal().getRecipes().isEmpty());

        // Message should still show consumed message
        assertTrue(result.contains("Item consumed."));
        assertFalse(result.contains("discovers a new recipe"));
    }
}