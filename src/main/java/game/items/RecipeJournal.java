package game.items;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actions.ReadJournalAction;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents a journal that stores discovered cooking recipes for the player.
 * <p>
 * The journal keeps a list of unique recipe strings that can be viewed by the player.
 * It provides an action that allows the owner to read the stored recipes.
 */
public class RecipeJournal extends Item {
    private final List<String> recipes = new ArrayList<>();


    /**
     * Creates a RecipeJournal item. By default, it is not portable, meaning
     * it cannot be dropped once obtained.
     */
    public RecipeJournal() {
        super("Recipe Journal", '☀', false); // not portable (player cannot drop)
        // If you want the player to be able to drop it, change to true
    }

    /**
     * Adds a new recipe into the journal.
     *
     * @param recipe the recipe string to store
     */
    public void addRecipe(String recipe) {
        recipes.add(recipe);
    }

    /**
     * Returns a copy of all recipes stored in the journal.
     *
     * @return a list containing all saved recipes
     */
    public List<String> getRecipes() {
        return new ArrayList<>(recipes); // return copy for safety
    }

    /**
     * Checks if the journal already contains the specified recipe.
     *
     * @param recipe the recipe to check
     * @return true if the recipe exists, false otherwise
     */
    public boolean hasRecipe(String recipe) {
        return recipes.contains(recipe);
    }

    /**
     * Returns a formatted string of all recipes for display purposes.
     *
     * @return a string representation of the recipe journal
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("=== Recipe Journal ===\n");
        for (String r : recipes) {
            sb.append("- ").append(r).append("\n");
        }
        return sb.toString();
    }

    /**
     * Provides the available actions that the owner can perform on the journal,
     * such as reading its contents.
     *
     * @param owner the actor holding the journal
     * @param map   the game map the actor is on
     * @return an ActionList containing actions available for this item
     */
    public ActionList allowableActions(Actor owner, GameMap map) {
        return new ActionList(new ReadJournalAction());
    }
}
