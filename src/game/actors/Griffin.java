package game.actors;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.Behaviour;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import game.weapons.Claw;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Griffin extends MythicalCreature{
    public Map<Integer, Behaviour> behaviours = new TreeMap<>();

    public Griffin(String name, char displayChar, int hitPoints) {
        super(name, displayChar, hitPoints);
        this.setIntrinsicWeapon(new Claw());

        int statesIndexCounter = 0;
        ArrayList<Status> states = new ArrayList<>();
        states.add(new Angry(this));
        states.add(new Sleepy(this));
        states.add(new Poisonous(this));
    }

    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        for (Behaviour behaviour : behaviours.values()) {
            Action action = behaviour.generateAction(this, map);
            if(action != null)
                return action;
        }
        return new DoNothingAction();
    }


    @Override
    public void changeState(Actor actor) {

    }
}
