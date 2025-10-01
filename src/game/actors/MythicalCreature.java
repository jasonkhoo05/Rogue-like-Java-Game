package game.actors;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.capabilities.Status;
import game.State.StatusType;
import game.capabilities.StateChangeable;

import java.util.ArrayList;
import java.util.List;

public abstract class MythicalCreature extends Actor implements StateChangeable {
    int statesIndexCounter = -1;
    // A list of all possible states of this MythicalCreature
    List<StatusType> statesList = new ArrayList<>();

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
        this.statesList = new ArrayList<>();
    }

    @Override
    public void changeState(Actor actor) {
//        // Statuses automatically remove itself once it is finished
//        // So if MythicalCreature don't have any state anymore, it is time to change state
//        if (this.statuses().isEmpty()) {
//            // Loops back to initial state after all state have been transversed
//            this.statesIndexCounter = (this.statesIndexCounter + 1) % this.statesList.size();
//            this.addStatus(statesList.get(this.statesIndexCounter));
//        }
//    }
    }
}
