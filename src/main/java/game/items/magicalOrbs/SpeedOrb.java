package game.items.magicalOrbs;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actions.ActivateOrbAction;
import game.capabilities.OrbEffect;
import game.status.SpeedBoost;

public class SpeedOrb extends MagicalOrb implements OrbEffect {
    /**
     * Constructor
     */
    public SpeedOrb() {
        super("Speed Orb", 'S', 5);
    }

    /**
     * Applies the magical orb effect to the actor
     * @param actor the actor to be added to for the effects
     * @return a String that describes the effect has been applied
     */
    @Override
    public String applyEffect(Actor actor) {
        actor.addStatus(new SpeedBoost(5));
        return "speed boost effect has been applied.";
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
        ActionList actions = new ActionList(new ActivateOrbAction(this));
        if (owner.hasStatus(SpeedBoost.class)) {
            actions.add(SpeedBoost.getExtraMoveActions(owner, map.locationOf(owner)));
        }
        return actions;
    }
}
