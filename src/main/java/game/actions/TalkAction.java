package game.actions;


import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.capabilities.Talkable;

public class TalkAction extends Action {

    private final Talkable talkable;
    private final String context;

    public TalkAction(Talkable talkable, String context) {
        this.talkable = talkable;
        this.context = context;
    }

    /**
     * Called by the engine when this action is executed.
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        // Calls the Talkable (WiseMan) to generate AI speech
        String speech = talkable.performMonologue(context);
        return actor + " says " + ": \"" + speech + "\"";
    }

    /**
     * Shown in the menu (if player can choose this action)
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " talks to " + talkable;
    }
}
