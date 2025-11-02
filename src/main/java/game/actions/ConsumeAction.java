package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.Ability;
import game.ai.RecipeGenerator;
import game.capabilities.Consumable;
import game.capabilities.HasRecipeJournal;

public class ConsumeAction extends Action {
    private final Item item;
    private final RecipeGenerator recipeGenerator;


    public ConsumeAction(Item item) {
        this(item, new RecipeGenerator());
    }

    // Test-friendly constructor
    public ConsumeAction(Item item, RecipeGenerator recipeGenerator) {
        this.item = item;
        this.recipeGenerator = recipeGenerator;
    }

    /**
     * Executes the action.
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return result
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        // Get the consumable capability
        var maybe = item.asCapability(Consumable.class);
        if (maybe.isEmpty()) {
            return actor + " can't consume " + item + ".";
        }

        // Apply item effect
        String result = maybe.get().consumedBy(actor);

        // Remove item from inventory if carried, else from the ground tile
        if (actor.getItemInventory().contains(item)) {
            actor.removeItemFromInventory(item);   //player path
        } else {
            Location here = map.locationOf(actor);
            if (here.getItems().contains(item)) {
                here.removeItem(item);               // Animal path (ground)
            }
        }
        // Extra line for ALL meadow-spawned animals
        String announce = "";
        if (actor.hasAbility(Ability.MEADOW_FORAGER)) {
            Location here = map.locationOf(actor);
            announce = String.format(
                    "Meadow-born %s at (%d,%d) forages %s.",
                    actor, here.x(), here.y(), item
            );
        }
        String recipeMessage = "";

        // Only generate recipe if actor is the player
        if (actor.hasAbility(Ability.IS_PLAYER)) {
            String newRecipe = recipeGenerator.generateRecipe(item.toString());


            // Safely add recipe to journal if actor has the capability
            actor.asCapability(HasRecipeJournal.class)
                    .ifPresent(journalCap -> journalCap.getRecipeJournal().addRecipe(newRecipe));

            recipeMessage = actor + " discovers a new recipe: " + newRecipe;
        }

//        // Engine prints the returned string
//        String finalMessage = announce.isEmpty() ? result : (announce + "\n" + result);
//        return finalMessage;


        // Build final message
        StringBuilder finalMessage = new StringBuilder();
        if (!announce.isEmpty()) {
            finalMessage.append(announce).append("\n");
        }
        finalMessage.append(result);
        if (!recipeMessage.isEmpty()) {
            finalMessage.append("\n").append(recipeMessage);
        }

        return finalMessage.toString();
    }

    /**
     * Shows the description for the menu.
     * @param actor The actor performing the action.
     * @return the description for the menu
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " consumes " + item;
    }
}
