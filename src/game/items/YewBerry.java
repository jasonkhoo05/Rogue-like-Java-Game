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

public class YewBerry extends Item implements Consumable {
    final int FINAL_HEALTH_VALUE = 0;

    /**
     * Constructor
     */
    public YewBerry() {
        super("Yew Berry", 'x', true);
    }



    /**
     * Defines the logic of being consumed
     *
     * @param actor the actor consuming this
     * @return the description that this has been consumed by the actor
     */
    public String consumedBy(Actor actor) {
        actor.modifyAttribute(BaseAttributes.HEALTH, ActorAttributeOperation.UPDATE, FINAL_HEALTH_VALUE);

        return actor + " eats the yew berry and they fell unconscious";
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
        ActionList actions = new ActionList();
        // can eat
        actions.add(new ConsumeAction(this));
        // add coating
        for (var a : game.actions.CoatWeaponAction.buildFor(
                owner,
                new game.items.weapons.coating.YewberryCoating(),
                this // coating by the item, will be consumed
        )) {
            actions.add(a);
        }

        return actions;
    }
}
