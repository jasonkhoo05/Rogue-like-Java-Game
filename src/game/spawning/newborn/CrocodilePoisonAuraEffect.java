package game.spawning.newborn;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Location;
import game.Species;
import game.status.Poisoned;
import game.spawning.newborn.SpawnContextEffect;

import java.util.function.Consumer;

/**
 * When a CROCODILE is spawned, poison every actor on the spawner's surrounding tiles.
 * Duration = 'turns', Damage/turn = 'damagePerTurn'.
 * Logs via the provided Consumer<String> (pass display::print to use Display.print).
 */
public class CrocodilePoisonAuraEffect implements SpawnContextEffect {
    private final int turns;
    private final int damagePerTurn;
    private final Consumer<String> log; // e.g., display::print

    public CrocodilePoisonAuraEffect(int turns, int damagePerTurn, Consumer<String> log) {
        this.turns = turns;
        this.damagePerTurn = damagePerTurn;
        this.log = log;
    }

    @Override
    public void apply(Actor newborn, Location spawnerLocation) {
        // Only trigger if the newborn is a CROCODILE
        if (!newborn.hasAbility(Species.CROCODILE)) return;

        // Poison any actor in surrounding tiles (excludes center/spawner tile)
        spawnerLocation.getExits().forEach(exit -> {
            Location adj = exit.getDestination();
            if (adj.containsAnActor()) {
                Actor target = adj.getActor();
                // Do NOT poison the newborn; spec says "within the surroundings"
                if (target != null && target != newborn) {
                    target.addStatus(new Poisoned(turns, damagePerTurn));

                    if (log != null) {
                        // Using Display.print (no newline). Add "\n" if you want line breaks.
                        log.accept("[CROCODILE AOE] " + newborn + " poisons " +
                                " for " + turns + " turns (" + damagePerTurn + " dmg/turn). ");
                    }
                }
            }
        });


    }
}
