package game.actors;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import game.ai.Monologue;
import game.capabilities.Talkable;

public class WiseMan extends Actor implements Talkable {

    private final Monologue monologueAI;

    public WiseMan(String name, char displayChar, int hitPoints) {
        super(name, displayChar, hitPoints);
        this.monologueAI = new Monologue();
    }

    /**
     * Generate AI monologue as an Action if needed, otherwise do nothing.
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        // Here, we don't want to auto-perform speech, so return null (no turn action)
        return null;
    }


    /**
     * Generate AI monologue based on context.
     */
    @Override
    public String performMonologue(String context) {
        String prompt = context != null ? context :
                "write me one short 20-word simple monologue an explorer will say and go next line if sentence reaches 10 words.";
        return monologueAI.generate(prompt);
    }

    @Override
    public String toString() {
        return "Wise Man";
    }
}

