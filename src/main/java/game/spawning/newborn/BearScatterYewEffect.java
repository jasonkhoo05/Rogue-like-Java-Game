package game.spawning.newborn;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.Species;
import game.items.YewBerry;


import java.util.Random;
import java.util.function.Consumer;

/**
 * When a BEAR is spawned, each exit of the spawner tile has a chance to receive a YewBerry.
 * Probability is evaluated independently per exit (e.g., 0.5 => 50% per exit).
 * Logs to the provided Consumer (usually Display::println).
 */
public class BearScatterYewEffect implements SpawnContextEffect {

    private final double chancePerExit;
    private final Random rng = new Random();
    private final Consumer<String> log;

    /**
     * @param chancePerExit probability in [0,1] applied to each exit
     * @param log logger for terminal messages (pass msg -> display.println(msg)); if null, no-op
     */
    public BearScatterYewEffect(double chancePerExit, Consumer<String> log) {
        this.chancePerExit = Math.max(0.0, Math.min(1.0, chancePerExit));
        this.log = (log != null) ? log : s -> {};
    }

    @Override
    public void apply(Actor newborn, Location spawnerLocation) {
        // Only apply when the newborn is a bear
        if (!newborn.hasAbility(Species.BEAR)) return;

        var exits = spawnerLocation.getExits();
        if (exits.isEmpty()) return;

        int dropped = 0;
        for (Exit e : exits) {
            if (rng.nextDouble() < chancePerExit) {
                var dest = e.getDestination();
                dest.addItem(new YewBerry());
                dropped++;
                log.accept(String.format("[SPAWN] %s at (%d,%d): YewBerry dropped at exit → (%d,%d)",
                        newborn, spawnerLocation.x(), spawnerLocation.y(), dest.x(), dest.y()));
            }
        }
        if (dropped == 0) {
            log.accept(String.format("[SPAWN] %s at (%d,%d): no YewBerry dropped this time (chance=%.0f%%/exit)",
                    newborn, spawnerLocation.x(), spawnerLocation.y(), 100*chancePerExit));
        }
    }
}
