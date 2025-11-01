package game.items.magicalOrbs;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actions.ActivateOrbAction;
import game.capabilities.OrbEffect;

public abstract class MagicalOrb extends Item implements OrbEffect {
    private final int duration;

    /**
     * Constructor
     */
    public MagicalOrb(String name, char displayChar, int duration) {
        super(name, displayChar, true);
        this.duration = duration;
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
        return new ActionList(new ActivateOrbAction(this));
    }

    /**
     * Getter method for the duration of the magical orb
     * @return the duration of the magical orb
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Applies the magical orb effect to the actor
     * @param actor the actor to be added to for the effects
     * @return a String that describes the effect has been applied
     */
    public abstract String applyEffect(Actor actor);

}
