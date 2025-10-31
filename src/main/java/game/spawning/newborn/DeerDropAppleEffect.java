package game.spawning.newborn;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.Species;
import game.items.Apple;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

/** When a Deer spawns, drop one Apple on a random exit of the spawner tile, and log it. */
public class DeerDropAppleEffect implements SpawnContextEffect {
    private final Consumer<String> log;   // injected logger (Display::println)

    /** Pass msg -> display.println(msg) from Earth. If null, no-op logging. */
    public DeerDropAppleEffect(Consumer<String> log) {
        this.log = (log != null) ? log : s -> {};
    }

    @Override
    public void apply(Actor newborn, Location spawnerLocation) {
        // Only for deer spawns
        if (!newborn.hasAbility(Species.DEER)) return;

        List<Exit> exits = spawnerLocation.getExits();
        if (exits.isEmpty()) return;

        Exit pick = exits.get(ThreadLocalRandom.current().nextInt(exits.size()));
        pick.getDestination().addItem(new Apple());

        log.accept(String.format(
                "[SPAWN] %s spawned at (%d,%d) -> dropped Apple",
                newborn, spawnerLocation.x(), spawnerLocation.y()
        ));
    }
}
