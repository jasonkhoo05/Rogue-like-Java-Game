package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import game.capabilities.Tameable;

public class TameAction extends Action {
    private Tameable tameable;
    private Item item;

    /**
     * Constructor
     *
     * @param tameable the tameable actor
     * @param item the item used to tame the tameable actor
     */
    public TameAction(Tameable tameable, Item item) {
        this.tameable = tameable;
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
        return this.tameable.tameBy(actor, item);
    }

    /**
     * Shows the description for the menu.
     * @param actor The actor performing the action.
     * @return the description for the menu
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " tames " + tameable + " with " + this.item;
    }
}
