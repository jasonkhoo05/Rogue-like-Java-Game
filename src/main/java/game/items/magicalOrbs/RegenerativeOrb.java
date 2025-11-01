package game.items.magicalOrbs;

import edu.monash.fit2099.engine.actors.Actor;
import game.capabilities.OrbEffect;
import game.status.Regenerative;

public class RegenerativeOrb extends MagicalOrb implements OrbEffect {
    /**
     * Constructor
     */
    public RegenerativeOrb() {
        super("Regenerative Orb", 'R', 5);
    }

    /**
     * Applies the magical orb effect to the actor
     * @param actor the actor to be added to for the effects
     * @return a String that describes the effect has been applied
     */
    @Override
    public String applyEffect(Actor actor) {
        actor.addStatus(new Regenerative(actor,5, 1));
        return "Regenerative effect has been applied.";
    }
}
