package game.actors;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Behaviour;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import game.weapons.Claw;

import java.util.Map;
import java.util.TreeMap;

public class Griffin extends MythicalCreature{
    public Map<Integer, Behaviour> behaviours = new TreeMap<>();

    public Griffin(String name, char displayChar, int hitPoints) {
        super(name, displayChar, hitPoints);
        this.setIntrinsicWeapon(new Claw());

        // Adding all the possible states of Griffin to the list of states for changeState to loop through
        this.statesList.add(new Angry(this));
        this.statesList.add(new Sleepy(this));
        this.statesList.add(new Poisonous(this));
    }

    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        changeState(this);
        for (Behaviour behaviour : behaviours.values()) {
            Action action = behaviour.generateAction(this, map);
            if(action != null)
                return action;
        }
        return new DoNothingAction();
    }
}
