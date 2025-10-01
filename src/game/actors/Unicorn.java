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
import game.State.Aegis;
import game.State.Radiant;
import game.State.StatusType;
import game.State.Vigor;
import game.behaviours.WanderBehaviour;


import java.util.Map;
import java.util.TreeMap;


public class Unicorn extends MythicalCreature {
    public Map<Integer, Behaviour> behaviours = new TreeMap<>();
    private Status currentState = null;
    private StatusType currentType = null;
    private Player player;


    public Unicorn(String name, char displayChar, int hitPoints) {
        super(name, displayChar, hitPoints);
        this.behaviours.put(999, new WanderBehaviour());

        // Adding all the possible states of Unicorn to the list of states for changeState to loop through
        this.statesList.add(StatusType.RADIANT);
        this.statesList.add(StatusType.AEGIS);
        this.statesList.add(StatusType.VIGOR);
    }

    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        // Move status tick logic here if not already done by engine
        for (Status status : this.statuses()) {
            status.tickStatus(this, map.locationOf(this));
        }
        changeState(this);

//        // Apply Vigor to Player only if Unicorn is in VIGOR state
//        if (currentType == StatusType.VIGOR && player != null) {
//            player.addStatus(new Vigor(5,player)); // lasts 3 turns
//            display.println(player + " is now in VIGOR state!");
//        }
        if (currentType == StatusType.VIGOR) {
            for (int x : map.getXRange()) {
                for (int y : map.getYRange()) {
                    Actor actor = map.at(x, y).getActor();
                    if (actor != null && actor.hasAbility(Ability.IS_PLAYER)) {
                        // Give the Player a new Vigor status
                        actor.addStatus(new Vigor(5)); // pass actor reference safely
                        display.println(actor + " is now in VIGOR state!");
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
    public void changeState(Actor actor) {
        if (statesList.isEmpty()) return;

        // If no state yet, start at index 0
        if (currentState == null) {
            currentType = statesList.get(0);
            currentState = createState(statesList.get(0));
            addStatus(currentState);
            System.out.println("Transformed to " + currentState.getClass().getSimpleName() + " state!");
            return;
        }

        // Only change state if the current one is no longer active
        if (currentState.isStatusActive()) {
            return; // Still active, keep current state
        }


        // Remove current state
        this.removeStatus(currentState);
        System.out.println(currentType + " fades away.");

        // Find current index by class type
        int currentIndex = statesList.indexOf(currentType);
        int nextIndex = currentIndex;

        // Define per-state probabilities (in %)
        int[] probabilities = {100, 100, 100};


        // Roll probability
        double roll = Math.random() * 100;
        if (roll < probabilities[currentIndex]) {

            nextIndex = (currentIndex + 1) % statesList.size();
        }

        // Create new state from StatusType
        currentType = statesList.get(nextIndex);
        currentState = createState(currentType);
        addStatus(currentState);
        System.out.println("Transformed to " + currentType + " state!");
    }

    private Status createState(StatusType type) {
        return switch (type) {
            case RADIANT -> new Radiant(1);
            case AEGIS -> new Aegis(1);
            case VIGOR -> new Vigor(5);
        };
    }
}
