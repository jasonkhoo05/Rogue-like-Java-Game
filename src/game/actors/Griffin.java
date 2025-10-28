package game.actors;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.Behaviour;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import game.Ability;
import game.state.*;
import game.behaviours.HostileBehaviour;
import game.behaviours.SleepyBehaviour;
import game.behaviours.WanderBehaviour;
import game.weapons.Claw;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Griffin extends MythicalCreature{
    public Map<Integer, Behaviour> behaviours = new TreeMap<>();
    List<StatusTypeGriffin> statesListGriffin = new ArrayList<>();
    private Status currentState = null;
    private StatusTypeGriffin currentType = null;

    public Griffin(String name, char displayChar, int hitPoints) {
        super(name, displayChar, hitPoints);
        this.setIntrinsicWeapon(new Claw());
        this.enableAbility(Ability.CAN_ATTACK);

        // Adding all the possible states of Griffin to the list of states for changeState to loop through
        this.statesListGriffin.add(StatusTypeGriffin.ANGRY);
        this.statesListGriffin.add(StatusTypeGriffin.SLEEPY);
        this.statesListGriffin.add(StatusTypeGriffin.DESICCATION);
    }

    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        changeState(this, display);
        for (Behaviour behaviour : behaviours.values()) {
            Action action = behaviour.generateAction(this, map);
            if(action != null)
                return action;
        }
        return new DoNothingAction();
    }

    @Override
    public void changeState(Actor actor, Display display) {
        if (statesListGriffin.isEmpty()) {
            return;
        }

        // If no state yet, start at index 0
        if (currentState == null) {
            currentType = statesListGriffin.get(0);
            currentState = createState(currentType);
            addStatus(currentState);

            // Add behaviours for the new state
            applyStateBehaviours(currentType);

            display.println(this.getClass().getSimpleName() + " transformed to " + currentState.getClass().getSimpleName() + " state!");
            return;
        }

        // Only change state if the current one is no longer active
        if (currentState.isStatusActive()) {
            return; // Still active, keep current state
        }


        // Remove current state
//        this.removeStatus(currentState);
        display.println(currentType + " fades away.");

        // Find current index by class type
        int currentIndex = statesListGriffin.indexOf(currentType);
        int nextIndex = currentIndex;

        // Define per-state probabilities (in %)
        int[] probabilities = {50 , 100, 60};


        // Roll probability
        double roll = Math.random() * 100;
        if (roll < probabilities[currentIndex]) {

            nextIndex = (currentIndex + 1) % statesListGriffin.size();
        }

        // Create new state from StatusType
        currentType = statesListGriffin.get(nextIndex);
        currentState = createState(currentType);
        addStatus(currentState);
        // Add behaviours for the new state
        applyStateBehaviours(currentType);
        display.println(this.getClass().getSimpleName() + " transformed to " + currentType + " state!");
    }

    private Status createState(StatusTypeGriffin type) {
        return switch (type) {
            case ANGRY -> new Angry(3);
            case SLEEPY -> new Sleepy(3);
            case DESICCATION -> new Desiccation(3);
        };
    }

    private void applyStateBehaviours(StatusTypeGriffin type) {
        this.behaviours.clear();
        switch (type) {
            case ANGRY -> {
                this.behaviours.put(1, new HostileBehaviour());
                this.behaviours.put(999, new WanderBehaviour());
            }
            case SLEEPY -> {
                this.behaviours.put(1, new SleepyBehaviour());
                this.behaviours.put(999, new WanderBehaviour());
            }
            case DESICCATION -> {
                this.behaviours.put(999, new WanderBehaviour());
            }
        }
    }
}
