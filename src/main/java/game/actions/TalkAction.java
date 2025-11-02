package game.actions;


import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.capabilities.Talkable;

/**
 * The {@code TalkAction} class represents an action where an actor
 * initiates a conversation or dialogue with a {@link Talkable} entity.
 * <p>
 * When executed, this action calls the Talkable to generate an AI monologue
 * based on the provided context and returns it as a message.
 * </p>
 */
public class TalkAction extends Action {

    private final Talkable talkable;
    private final String context;

    /**
     * Constructs a {@code TalkAction} for a given {@link Talkable} actor and context.
     *
     * @param talkable the entity capable of performing a monologue
     * @param context  a description or prompt to guide the generated monologue
     */
    public TalkAction(Talkable talkable, String context) {
        this.talkable = talkable;
        this.context = context;
    }

    /**
     * Executes the action when called by the engine.
     * <p>
     * This method calls the Talkable's Talkable performMonologue(String) method
     * with the specified context and returns a formatted string indicating
     * what the actor says.
     * </p>
     *
     * @param actor the actor performing this action
     * @param map   the {@link GameMap} where the action occurs
     * @return a string describing the actor's speech
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        // Calls the Talkable (WiseMan) to generate AI speech
        String speech = talkable.performMonologue(context);
        return actor + " says " + ": \"" + speech + "\"";
    }

    /**
     * Provides the description for the menu.
     * <p>
     * This is displayed when the player can choose to perform this action.
     * </p>
     *
     * @param actor the actor performing the action
     * @return a string describing the menu option
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " talks to " + talkable;
    }
}
