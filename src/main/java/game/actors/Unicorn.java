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
import game.behaviours.HealBehaviour;
import game.state.Aegis;
import game.state.Radiant;
import game.state.StatusTypeUnicorn;
import game.state.Vigor;
import game.behaviours.WanderBehaviour;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class Unicorn extends MythicalCreature {
    public Map<Integer, Behaviour> behaviours = new TreeMap<>();
    private Status currentState = null;
    private StatusTypeUnicorn currentType = null;
    private Player player;
    List<StatusTypeUnicorn> statesList = new ArrayList<>();

    public Unicorn(String name, char displayChar, int hitPoints) {
        super(name, displayChar, hitPoints);
        this.behaviours.put(999, new WanderBehaviour());

        // Adding all the possible states of Unicorn to the list of states for changeState to loop through
        this.statesList.add(StatusTypeUnicorn.RADIANT);
        this.statesList.add(StatusTypeUnicorn.AEGIS);
        this.statesList.add(StatusTypeUnicorn.VIGOR);

        this.behaviours.put(10, new HealBehaviour(new Display()));

    }

    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {

        changeState(this,display);

        if (currentType == StatusTypeUnicorn.VIGOR) {
            for (int x : map.getXRange()) {
                for (int y : map.getYRange()) {
                    Actor actor = map.at(x, y).getActor();
                    if (actor != null && actor.hasAbility(Ability.IS_PLAYER)) {
                        // Give the Player a new Vigor status
                        actor.addStatus(new Vigor(3)); // pass actor reference safely
                        display.println(this.getClass().getSimpleName() + " is now in VIGOR state!");
                    }
                }
            }
        }

        if (currentType == StatusTypeUnicorn.AEGIS) {
            for (int x : map.getXRange()) {
                for (int y : map.getYRange()) {
                    Actor actor = map.at(x, y).getActor();
                    if (actor != null && actor.hasAbility(Ability.IS_PLAYER)) {
                        actor.addStatus(new Aegis(3)); // e.g., lasts 3 turns
                        display.println(actor + " is now protected by AEGIS!");
                    }
                }
            }
        }

        for (Behaviour behaviour : behaviours.values()) {
            Action action = behaviour.generateAction(this, map);
            if (action != null)
                return action;
        }
        return new DoNothingAction();
    }


    @Override
    public void changeState(Actor actor, Display display) {
        if (statesList.isEmpty()) return;

        // If no state yet, start at index 0
        if (currentState == null) {
            currentType = statesList.get(0);
            currentState = createState(statesList.get(0));
            addStatus(currentState);
            display.println(this.getClass().getSimpleName() + " transformed to " + currentType + " state!");
            return;
        }

        // Only change state if the current one is no longer active
        if (currentState.isStatusActive()) {
            return; // Still active, keep current state
        }


        // Remove current state
        display.println(currentType + " fades away.");

        // Find current index by class type
        int currentIndex = statesList.indexOf(currentType);
        int nextIndex = currentIndex;

        // Define per-state probabilities (in %)
        int[] probabilities = {50, 60, 70};


        // Roll probability
        double roll = Math.random() * 100;
        if (roll < probabilities[currentIndex]) {

            nextIndex = (currentIndex + 1) % statesList.size();
        }

        // Create new state from StatusType
        currentType = statesList.get(nextIndex);
        currentState = createState(currentType);
        addStatus(currentState);

        applyStateBehaviours(currentType, display);

        display.println("Unicorn transformed to " + currentType + " state!");
    }

    private Status createState(StatusTypeUnicorn type) {
        return switch (type) {
            case RADIANT -> new Radiant(3);
            case AEGIS -> new Aegis(3);
            case VIGOR -> new Vigor(3);
        };
    }

    private void applyStateBehaviours(StatusTypeUnicorn type, Display display) {
        this.behaviours.clear();
        switch (type) {
            case RADIANT -> {
                this.behaviours.put(1, new HealBehaviour(display));
                this.behaviours.put(999, new WanderBehaviour());
            }
            case AEGIS -> {
                // Maybe give defensive behaviour later if needed
                this.behaviours.put(999, new WanderBehaviour());
            }
            case VIGOR -> {
                // Could give buff behaviour later if needed
                this.behaviours.put(999, new WanderBehaviour());
            }
        }
    }
}
