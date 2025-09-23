package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;

import java.util.Random;

/**
 * This class is copied from the demo
 */
public class SleepAction extends Action {
    Random random = new Random();
    private int sleepTime = random.nextInt(6, 11);

    /**
     * Executes the action.
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return result
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        sleepTime--;
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return actor + " is sleeping for " + sleepTime + " rounds";
    }

    /**
     * Shows the description for the menu.
     * @param actor The actor performing the action.
     * @return the description for the menu
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " sleeps for " + sleepTime + " turns";
    }

    /**
     * Return the next SleepAction if duration of sleep is not finished
     * @return the next SleepAction
     */
    @Override
    public Action getNextAction() {
        if (sleepTime > 0)
            return this;
        return null;
    }
}
