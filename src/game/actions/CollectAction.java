package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;

public class CollectAction extends Action {
    private final Item item;
    private final Actor master;
    private final Location locationOfitem;

    /**
     * Constructor
     *
     * @param master the master to pass the item to
     * @param item the item to be passed
     * @param locationOfItem the location of the item
     */
    public CollectAction(Actor master, Item item, Location locationOfItem) {
        this.master = master;
        this.item = item;
        this.locationOfitem = locationOfItem;
    }

    /**
     * Executes the action.
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return result
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        this.locationOfitem.removeItem(this.item);
        this.master.addItemToInventory(this.item);
        return menuDescription(actor);
    }

    /**
     * Shows the description for the menu.
     * @param actor The actor performing the action.
     * @return the description for the menu
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " picks up the " + item + " and passes it to " + this.master;
    }
}
