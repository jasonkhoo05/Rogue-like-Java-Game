package game.items.magicalOrbs;

import edu.monash.fit2099.engine.actors.Actor;
import game.capabilities.OrbEffect;
import game.status.HeatResistant;

public class HeatResistantOrb extends MagicalOrb implements OrbEffect {
    /**
     * Constructor
     */
    public HeatResistantOrb() {
        super("Heat Resistant Orb", 'H', 5);
    }

    /**
     * Applies the magical orb effect to the actor
     * @param actor the actor to be added to for the effects
     * @return a String that describes the effect has been applied
     */
    @Override
    public String applyEffect(Actor actor) {
        actor.addStatus(new HeatResistant(5));
        return "Heat resistant effect has been applied.";
    }
}
