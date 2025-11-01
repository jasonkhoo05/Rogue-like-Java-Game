package game.actions;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.Ability;
import game.ai.RecipeGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        // Actor has IS_PLAYER ability
        Actor player = new FakeActor(true);

        // Mock recipe generator output
        when(mockRecipeGenerator.generateRecipe("TestBerry")).thenReturn("MockRecipe: Berry Soup");

        ConsumeAction consumeAction = new ConsumeAction(testItem, mockRecipeGenerator);
        String result = consumeAction.execute(player, mockMap);

        // Verify recipe generator called once
        verify(mockRecipeGenerator, times(1)).generateRecipe("TestBerry");

        // Ensure both parts of message are included
        assertTrue(result.contains("Item consumed."));
        assertTrue(result.contains("discovers a new recipe: MockRecipe: Berry Soup"));
    }

    @Test
    void testRecipeNotGeneratedWhenActorIsNotPlayer() {
        Actor player = new FakeActor(false);

        ConsumeAction consumeAction = new ConsumeAction(testItem, mockRecipeGenerator);
        String result = consumeAction.execute(player, mockMap);

        // Verify recipe generator never called
        verify(mockRecipeGenerator, never()).generateRecipe(anyString());

        // Ensure recipe message is absent
        assertFalse(result.contains("discovers a new recipe"));
        assertTrue(result.contains("Item consumed."));
    }
}