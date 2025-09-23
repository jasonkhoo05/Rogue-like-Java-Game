package game.items;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperation;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actions.DrinkAction;
import game.actors.attributes.PlayerAttribute;
import game.capabilities.Drinkable;

public class Bottle extends Item implements Drinkable {
    final int INCREASE_HYDRATION_VALUE = 4;
    int drinkCounter = 0;
    final int MAX_COUNT = 5;
    /***
     * Constructor.
     *  @param name the name of this Item
     * @param displayChar the character to use to represent this item if it is on the ground
     * @param portable true if and only if the Item can be picked up
     */
    public Bottle(String name, char displayChar, boolean portable) {
        super(name, displayChar, portable);
    }

    /**
     * Defines the logic of being drunk by the actor
     *
     * @param actor the actor drinking this
     * @return the description that this has been drank by the actor
     */
    public String drunkBy(Actor actor) {
        actor.modifyAttribute(PlayerAttribute.HYDRATION, ActorAttributeOperation.INCREASE, INCREASE_HYDRATION_VALUE);
        this.drinkCounter ++;
        return actor + " drinks from the bottle and restores " + INCREASE_HYDRATION_VALUE + " hydration points.";
    }

    /**
     * List of allowable actions that the item can perform to its owner
     * or to the current map while being carried by an actor
     * Example #1: a healing item can have a special skill that can increase the current actor's hitpoints.
     *
     * @param owner the actor that owns the item
     * @param map the map where the actor is performing the action on
     * @return an unmodifiable list of Actions
     */
    @Override
    public ActionList allowableActions(Actor owner, GameMap map) {
        if (this.drinkCounter < MAX_COUNT) {
            return new ActionList(new DrinkAction(this));

        }
        return new ActionList();
    }
}
