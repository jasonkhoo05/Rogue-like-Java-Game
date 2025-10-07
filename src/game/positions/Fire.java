package game.positions;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.status.BurningManager;

/**
 * Temporary fire tile (‘^’).
 * - Immediately and permanently turns the cell into Dirt (after the fire expires).
 * - Actors standing here get a fresh 5-dmg-for-5-turns burn stack on each entry.
 * - The flame itself lasts for 'life' ticks (visual/environmental), default 3.
 * @author Daffa Arrazy
 */
public class Fire extends Ground {
    private int life; // how many ticks fire remains visually on the ground

    public Fire(int life) {
        super('^', "Fire");
        this.life = Math.max(1, life);
    }

    @Override
    public void tick(Location location) {
        // When fire expires, replace with Dirt
        if (--life <= 0) {
            location.setGround(new Dirt());
        }
        // If an actor is here *this tick*, (re)apply a burn stack (5 dmg for 5 turns)
        Actor actor = location.getActor();
        if (actor != null && actor.isConscious()) {
            BurningManager.addBurn(actor, 5, 5);
        }
    }

    @Override
    public boolean canActorEnter(Actor actor) {
        // Fire is enterable; stepping on it adds/refreshes burn stacks in tick().
        return true;
    }
}
