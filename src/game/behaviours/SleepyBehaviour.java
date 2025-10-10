package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.Behaviour;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actions.SleepAction;

import java.util.Random;

public class SleepyBehaviour implements Behaviour {
    private final Random random = new Random();
    private final int SLEEPCHANCE = 30;
    private boolean sleeping = false;
    private SleepAction sleepAction = new SleepAction();

    /**
     * Generates the action to be performed by the actor
     *
     * @param actor the Actor acting
     * @param map the GameMap containing the Actor
     * @return the action to be performed by the actor
     */
    @Override
    public Action generateAction(Actor actor, GameMap map) {
        if (!(random.nextInt(100) <= SLEEPCHANCE) && !sleeping) {
            sleeping = true;
            return sleepAction;
        }
        // Handle multi-turn Actions
        if (sleepAction.getNextAction() != null)
            return sleepAction.getNextAction();
        return null;
    }
}
