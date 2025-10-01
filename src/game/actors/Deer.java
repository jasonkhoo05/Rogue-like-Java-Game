package game.actors;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.Behaviour;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import game.Ability;
import game.actions.AttackAction;
import game.actions.TameAction;
import game.behaviours.CollectBehaviour;
import game.behaviours.FollowBehaviour;
import game.behaviours.WanderBehaviour;
import game.capabilities.Tameable;

import java.util.Map;
import java.util.TreeMap;

public class Deer extends Animal {
    /**
     * Constructor
     *
     * @param name        the name of the Actor
     * @param displayChar the character that will represent the Actor in the
     *                    display
     * @param hitPoints   the Actor's starting hit points
     */
    public Deer(String name, char displayChar, int hitPoints) {
        super(name, displayChar, hitPoints);
        this.behaviours.put(999, new WanderBehaviour());
    }

    /**
     * Defines the logic of after being tamed
     *
     * @param actor The actor that tames this
     * @param item The item that the actor used for taming this
     * @return the description that this has been tamed by the actor
     */
    @Override
    public String tameBy(Actor actor, Item item) {
        actor.removeItemFromInventory(item);
        this.behaviours.clear();
        this.behaviours.put(1, new CollectBehaviour(actor));
        this.behaviours.put(2, new FollowBehaviour(actor));
        this.enableAbility(Ability.TAMED);
        return actor + " has tamed " + this + " using " + item;
    }
}
