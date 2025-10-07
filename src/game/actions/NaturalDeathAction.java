package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * Lets an unconscious actor execute the engine's natural-death flow.
 * World.processActorTurn(...) will print the returned string to the Display.
 */
public class NaturalDeathAction extends Action {
    @Override
    public String execute(Actor actor, GameMap map) {
        return actor.unconscious(map);
    }

    @Override
    public String menuDescription(Actor actor) {
        return actor + " collapses.";
    }
}
