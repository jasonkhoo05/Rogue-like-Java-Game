package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.items.magicalOrbs.MagicalOrb;

public class ActivateOrbAction extends Action {
    private final MagicalOrb magicalOrb;

    /**
     * Contructor
     * @param magicalOrb the magical orb item
     */
    public ActivateOrbAction(MagicalOrb magicalOrb) {
        this.magicalOrb = magicalOrb;
    }

    /**
     * Executes the action.
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return result
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        String stringResult = magicalOrb.applyEffect(actor);
        return actor + " used " + magicalOrb + " and " + stringResult;
    }

    /**
     * Shows the description for the menu.
     * @param actor The actor performing the action.
     * @return the description for the menu
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " activates " + magicalOrb;
    }
}
