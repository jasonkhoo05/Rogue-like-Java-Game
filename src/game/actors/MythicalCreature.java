package game.actors;

import edu.monash.fit2099.engine.actors.Actor;
import game.capabilities.StateChangeable;

public abstract class MythicalCreature extends Actor implements StateChangeable {
    // counter to determine when to change state
    int counter;
    /**
     * The constructor of the Actor class.
     *
     * @param name        the name of the Actor
     * @param displayChar the character that will represent the Actor in the
     *                    display
     * @param hitPoints   the Actor's starting hit points
     */
    public MythicalCreature(String name, char displayChar, int hitPoints) {
        super(name, displayChar, hitPoints);
        this.counter = 0;
    }
}
