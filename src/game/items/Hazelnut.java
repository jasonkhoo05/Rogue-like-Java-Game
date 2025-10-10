package game.items;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperation;
import edu.monash.fit2099.engine.actors.attributes.BaseAttributes;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import game.Ability;
import game.actions.ConsumeAction;
import game.capabilities.Consumable;

import java.util.Optional;

public class Hazelnut extends Item implements Consumable {
    final int INCREASE_MAX_HEALTH_VALUE = 1;

    /**
     * Constructor
     */
    public Hazelnut() {
        super("Hazelnut", 'n', true);
    }



    /**
     * Defines the logic of being consumed
     *
     * @param actor the actor consuming this
     * @return the description that this has been consumed by the actor
     */
    public String consumedBy(Actor actor) {
        actor.modifyStatsMaximum(BaseAttributes.HEALTH, ActorAttributeOperation.INCREASE, INCREASE_MAX_HEALTH_VALUE);

        return actor + " eats the hazelnut and increases their maximum health by " + INCREASE_MAX_HEALTH_VALUE;
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
        this.enableAbility(Ability.CAN_TAME);
        return new ActionList(new ConsumeAction(this));
    }
}
