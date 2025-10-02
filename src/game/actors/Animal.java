package game.actors;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttribute;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperation;
import edu.monash.fit2099.engine.actors.attributes.BaseAttributes;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.Ability;
import game.actors.attributes.AnimalAttribute;
import game.statuses.DecreaseWarmth;

/**
 * Base class for warm-blooded animals with warmth that decays per tick.
 */
public abstract class Animal extends Actor {

    protected Animal(String name, char displayChar, int hitPoints, int startingWarmth) {
        super(name, displayChar, hitPoints);

        // give animals their warmth attribute (cap 99 is enough; adjust if needed)
        this.addNewStatistic(AnimalAttribute.WARMTH, new BaseActorAttribute(99));
        this.modifyAttribute(AnimalAttribute.WARMTH, ActorAttributeOperation.UPDATE, startingWarmth);

        // optional clarity: mark as warm-blooded
        this.enableAbility(Ability.WARM_BLOODED);

        // attach a status that decreases warmth every tick and removes the actor at 0
        this.addStatus(new DecreaseWarmth());
    }


    /** print a compact stat line to the terminal */
    protected void printStats(GameMap map, Display display) {
        Location here = map.locationOf(this);
        int warmth = getAttribute(AnimalAttribute.WARMTH);

        display.println(
                String.format("[%s %c @(%d,%d) WARMTH=%d]",
                        this.name, this.getDisplayChar(), here.x(), here.y(),warmth)
        );
    }
}
