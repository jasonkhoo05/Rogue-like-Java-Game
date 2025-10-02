
package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * Executes the engine's natural unconscious flow and returns its message.
 * Used when an Actor is already unconscious at the start of its turn.
 */
public class NaturalDeathAction extends Action {
    @Override
    public String execute(Actor actor, GameMap map) {
        // This prints via World.processActorTurn → display.println(result)
        return actor.unconscious(map);
    }

    @Override
    public String menuDescription(Actor actor) {
        return actor + " collapses from the cold.";
    }
}
