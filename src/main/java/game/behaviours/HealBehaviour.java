package game.behaviours;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.Behaviour;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.Ability;
import game.actions.HealAction;

public class HealBehaviour implements Behaviour {
    private final Display display;

    public HealBehaviour(Display display) {
        this.display = display;
    }

    @Override
    public Action generateAction(Actor actor, GameMap map) {
        Location unicornLocation = map.locationOf(actor);

        // Look around adjacent tiles
        for (Exit exit : unicornLocation.getExits()) {
            Location destination = exit.getDestination();
            if (destination.containsAnActor()) {
                Actor nearbyActor = destination.getActor();

                // Heal only healable actors that have been marked by Radiant
                if (nearbyActor.hasAbility(Ability.HEALABLE) &&
                        nearbyActor.hasAbility(Ability.RECEIVED_HEAL)) {

                    // Disable the flag so it doesn't heal twice
                    nearbyActor.disableAbility(Ability.RECEIVED_HEAL);

                    display.println(actor + " radiates healing light towards " + nearbyActor + "!");
                    return new HealAction(nearbyActor, 10);
                }
            }
        }
        return null; // no valid heal target
    }
}




