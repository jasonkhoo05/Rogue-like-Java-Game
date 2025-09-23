package game.items;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.SleepAction;

public class Bedroll extends Item {
    /***
     * Constructor.
     *  @param name the name of this Item
     * @param displayChar the character to use to represent this item if it is on the ground
     * @param portable true if and only if the Item can be picked up
     */
    public Bedroll(String name, char displayChar, boolean portable) {
        super(name, displayChar, portable);
    }

    /**
     * List of allowable actions that can be performed on the item when it is on the ground
     * Example #1: a trap can return an action to disarm the trap.
     *
     * @param location the location of the ground on which the item lies
     * @return an unmodifiable list of Actions
     */
    @Override
    public ActionList allowableActions(Location location) {
        return new ActionList(new SleepAction());
    }
}
