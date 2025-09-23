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

public class Deer extends Actor implements Tameable {
    private Map<Integer, Behaviour> behaviours = new TreeMap<>();

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
     * Select and return an action to perform on the current turn.
     *
     * @param actions collection of possible Actions for this Actor
     * @param lastAction The Action this Actor took last turn. Can do
     * interesting things in conjunction with Action.getNextAction()
     * @param map the map containing the Actor
     * @param display the I/O object to which messages may be written
     * @return the Action to be performed
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        for (Behaviour behaviour : behaviours.values()) {
            Action action = behaviour.generateAction(this, map);
            if(action != null)
                return action;
        }
        return new DoNothingAction();
    }

    /**
     * Returns a new collection of the Actions that the otherActor can do to the
     * current Actor.
     *
     * @param otherActor the Actor that might be performing attack
     * @param direction String representing the direction of the other Actor
     * @param map current GameMap
     * @return A collection of Actions.
     */
    @Override
    public ActionList allowableActions(Actor otherActor, String direction, GameMap map) {
        ActionList actions = new ActionList();
        if(otherActor.hasAbility(Ability.CAN_ATTACK)){
            actions.add(new AttackAction(this, direction));
        }

        if (!this.hasAbility(Ability.TAMED)) {
            for (Item item : otherActor.getItemInventory()) {
                if (item.hasAbility(Ability.CAN_TAME)) {
                    actions.add(new TameAction(this, item));
                }
            }
        }

        return actions;
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
