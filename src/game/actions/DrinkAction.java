package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.capabilities.Drinkable;

public class DrinkAction extends Action {
    private Drinkable drinkable;

    /**
     * Contructor
     *
     * @param drinkable the drinkable item
     */
    public DrinkAction(Drinkable drinkable) {
        this.drinkable = drinkable;
    }

    /**
     * Executes the action.
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return result
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        return this.drinkable.drunkBy(actor);
    }

    /**
     * Shows the description for the menu.
     * @param actor The actor performing the action.
     * @return the description for the menu
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " drinks from " + drinkable;
    }
}
