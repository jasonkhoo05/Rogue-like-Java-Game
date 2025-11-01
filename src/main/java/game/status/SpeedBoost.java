package game.status;

import edu.monash.fit2099.engine.GameEntity;
import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.MoveActorAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;

import java.util.ArrayList;
import java.util.List;

public class SpeedBoost implements Status {
    private int duration;

    /**
     * Constructor
     * @param duration number of turns the status lasts
     */
    public SpeedBoost(int duration) {
        this.duration = duration;
    }

    /**
     * Called once per tick to update the status of the current entity.
     *
     * @param currEntity the entity this status is attached to
     */
    @Override
    public void tickStatus(GameEntity currEntity, Location location) {
        duration--;
    }

    /**
     * Indicates whether this status is still active.
     *
     * @return true if active, false otherwise
     */
    @Override
    public boolean isStatusActive() {
        return duration > 0;
    }

    /**
     * Gets the extra movement that the actor can make due to having a speed boost
     * @param actor the actor that have the speed boost status
     * @param here the current location of the actor
     * @return a list of movement actions that the actor can make
     */
    public static List<Action> getExtraMoveActions(Actor actor, Location here) {
        List<Action> actions = new ArrayList<>();

        for (Exit exit : here.getExits()) {
            Location next = exit.getDestination();

            if (next.canActorEnter(actor)) {
                // Depth-1 movement (normal)
                // Already processed by World

                // Depth-2 moves
                addRecursiveMoveActions(actor, actions, next, exit.getName(), 2);
            }
        }
        return actions;
    }

    /**
     * A helper method for getExtraMoveActions.
     * It processes the second movement action that the actor can make.
     * @param actor the actor that have the speed boost status
     * @param actions a list of movement actions that the actor can currently make
     * @param current the current location of the actor
     * @param initialDirection the initial direction of movement of the actor
     * @param depth the depth of this recursion function
     */
    private static void addRecursiveMoveActions(Actor actor, List<Action> actions, Location current, String initialDirection, int depth) {
        if (depth > 2) return;

        for (Exit nextExit : current.getExits()) {
            Location next = nextExit.getDestination();

            if (next.canActorEnter(actor)) {
                String direction = initialDirection + " -> " + nextExit.getName();
                actions.add(new MoveActorAction(nextExit.getDestination(), direction));
                addRecursiveMoveActions(actor, actions, next, initialDirection, depth + 1);
            }
        }
    }
}
