package game.items;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actions.ReadJournalAction;

import java.util.ArrayList;
import java.util.List;

public class RecipeJournal extends Item {
    private final List<String> recipes = new ArrayList<>();

    public RecipeJournal() {
        super("Recipe Journal", '☀', false); // not portable (player cannot drop)
        // If you want the player to be able to drop it, change to true
    }

    public void addRecipe(String recipe) {
        recipes.add(recipe);
    }

    public List<String> getRecipes() {
        return new ArrayList<>(recipes); // return copy for safety
    }

    public boolean hasRecipe(String recipe) {
        return recipes.contains(recipe);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("=== Recipe Journal ===\n");
        for (String r : recipes) {
            sb.append("- ").append(r).append("\n");
        }
        return sb.toString();
    }

    public ActionList allowableActions(Actor owner, GameMap map) {
        return new ActionList(new ReadJournalAction());
    }
}
