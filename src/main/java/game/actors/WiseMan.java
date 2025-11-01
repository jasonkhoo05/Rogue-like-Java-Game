package game.actors;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actions.TalkAction;
import game.ai.Monologue;
import game.capabilities.Talkable;


/**
 * The {@code WiseMan} class represents a non-player actor capable of speaking AI-generated monologues.
 * <p>
 * WiseMan implements the {@link Talkable} interface and generates a monologue every three turns.
 * Speech is produced using the {@link Monologue} AI service. On other turns, the WiseMan performs
 * no action.
 * </p>
 */
public class WiseMan extends Actor implements Talkable {

    private final Monologue monologueAI;
    private int turnCounter;


    /**
     * Constructs a {@code WiseMan} with the given name, display character, and hit points.
     * Initializes the AI monologue generator and turn counter.
     *
     * @param name the name of the WiseMan
     * @param displayChar the character representing the WiseMan on the map
     * @param hitPoints the hit points of the WiseMan
     */
    public WiseMan(String name, char displayChar, int hitPoints) {
        super(name, displayChar, hitPoints);
        this.monologueAI = new Monologue();
        this.turnCounter = 0;
    }

    /**
     * Determines the action the WiseMan performs on its turn.
     * <p>
     * Every three turns, a {@link TalkAction} is created with a prompt for AI-generated speech.
     * On other turns, the WiseMan performs {@link DoNothingAction}.
     * </p>
     *
     * @param actions the list of possible actions
     * @param lastAction the last action performed by this actor
     * @param map the {@link GameMap} the actor is on
     * @param display the display object to show messages
     * @return the action to perform this turn
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        turnCounter++;
        if (turnCounter % 3 == 0) {
            String prompt = "write me one short 20-word simple monologue an explorer will say and go next line if sentence reaches 10 words.";
            return new TalkAction(this, prompt);
        }
        // Otherwise, passive: do nothing
        return new DoNothingAction();
    }

    /**
     * Generates a monologue based on the given context using AI.
     *
     * @param context a custom prompt for generating the monologue; if null, a default wise man prompt is used
     * @return a {@link String} containing the AI-generated monologue
     */
    @Override
    public String performMonologue(String context) {
        String prompt = context != null ? context :
                "write me one short 20-word simple monologue a wise man will say and go next line if sentence reaches 10 words.";
        return monologueAI.generate(prompt);
    }

    /**
     * Returns the string representation of the WiseMan.
     *
     * @return "Wise Man"
     */
    @Override
    public String toString() {
        return "Wise Man";
    }
}

