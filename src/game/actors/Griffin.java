package game.actors;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.capabilities.Status;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import game.weapons.Claw;

import java.util.ArrayList;

public class Griffin extends MythicalCreature{
    public Griffin(String name, char displayChar, int hitPoints) {
        super(name, displayChar, hitPoints);
        this.setIntrinsicWeapon(new Claw());

        int statesIndexCounter = 0;
        ArrayList<Status> states = new ArrayList<>();
        states.add(new Angry());
        states.add(new Sleepy());
        states.add(new Poisonous());
    }

    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        return null;
    }


    @Override
    public void changeState(Actor actor) {

    }
}
