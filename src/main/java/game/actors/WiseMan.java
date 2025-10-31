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

public class WiseMan extends Actor implements Talkable {

    private final Monologue monologueAI;
    private int turnCounter;

    public WiseMan(String name, char displayChar, int hitPoints) {
        super(name, displayChar, hitPoints);
        this.monologueAI = new Monologue();
        this.turnCounter = 0;
    }

    /**
     * Generate AI monologue as an Action if needed, otherwise do nothing.
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

    @Override
    public String performMonologue(String context) {
        String prompt = context != null ? context :
                "write me one short 20-word simple monologue a wise man will say and go next line if sentence reaches 10 words.";
        return monologueAI.generate(prompt);
    }

    @Override
    public String toString() {
        return "Wise Man";
    }
}

