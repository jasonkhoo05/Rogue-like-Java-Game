package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.Ability;
import game.capabilities.Consumable;

public class ConsumeAction extends Action {
    private final Item item;


    /**
     * Constructor
     *
     * @param
     */
    public ConsumeAction(Item item) {
        this.item = item;

    }

    /**
     * Executes the action.
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return result
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        // Get the consumable capability (no instanceof)
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
        // Engine prints the returned string
        return announce.isEmpty() ? result : (announce + "\n" + result);
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
