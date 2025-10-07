
package game.spawning;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttribute;
import game.Ability;
import game.actors.attributes.PlayerAttribute;
import game.behaviours.ForageBehaviour;
import game.behaviours.SeekConsumablesBehaviour;
import game.capabilities.BehaviourHost;

public class ForagingEffect implements SpawnEffect {
    @Override
    public void apply(Actor newborn) {
        // ensure HYDRATION exists so Apple/Hazelnut/YewBerry effects work
        if (!newborn.hasStatistic(PlayerAttribute.HYDRATION)) {
            newborn.addNewStatistic(PlayerAttribute.HYDRATION, new BaseActorAttribute(99));
        }

        // Tag: this actor was spawned from a Meadow
        newborn.enableAbility(Ability.MEADOW_FORAGER);

        newborn.asCapability(BehaviourHost.class).ifPresent(host -> {
            // Eat immediately if standing on food
            host.putBehaviour(-1, new ForageBehaviour());
            // Hunt for food within radius (e.g., 5 tiles)   // eat immediately if on tile
            host.putBehaviour(0, new SeekConsumablesBehaviour(15));  // then move toward nearest food
        });
    }
}
