package game.spawning.newborn;

import edu.monash.fit2099.engine.GameEngineException;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.Bear;
import game.actors.Wolf;
import game.positions.Snow;
import game.positions.trees.ProximityYewBerryTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class WolfGrowYewTreeEffectTest {

    private DefaultGroundCreator gc;
    private GameMap map;

    @BeforeEach
    void setup() throws GameEngineException {
        gc = new DefaultGroundCreator();
        // Register only what the ASCII map needs
        gc.registerGround('.', Snow::new);

        map = new GameMap("Test", gc, Arrays.asList(
                "...",
                "...",
                "..."
        ));
    }

    @Test
    void growsOneProximityTreeOnRandomExit_whenNewbornIsWolf() throws GameEngineException {
        // Arrange: spawner tile in the middle so it has 8 exits
        Location spawner = map.at(1, 1);

        // A real Wolf from your codebase
        Wolf newbornWolf = new Wolf("Wolf", 'e', 100);

        // Count log lines (optional)
        AtomicInteger logs = new AtomicInteger(0);
        WolfGrowYewTreeEffect effect = new WolfGrowYewTreeEffect(msg -> logs.incrementAndGet());

        // Precondition: no exit has a ProximityYewBerryTree yet
        long preCount = spawner.getExits().stream()
                .filter(e -> e.getDestination().getGround().getClass().getSimpleName().equals("ProximityYewBerryTree"))
                .count();
        assertEquals(0, preCount, "No proximity yew tree should exist before applying effect.");

        // Act
        effect.apply(newbornWolf, spawner);

        // Assert: exactly one exit now has a ProximityYewBerryTree
        long postCount = spawner.getExits().stream()
                .filter(e -> e.getDestination().getGround().getClass().equals(ProximityYewBerryTree.class))
                .count();
        assertEquals(1, postCount, "Exactly one exit should become a ProximityYewBerryTree.");

        // Optional: there should be at least one log line
        assertTrue(logs.get() >= 1, "Expected at least one log line.");
    }

    @Test
    void doesNothing_whenNewbornIsNotWolf() throws GameEngineException {
        Location spawner = map.at(1, 1);
        Bear newbornBear = new Bear("Bear", 'B', 200);

        WolfGrowYewTreeEffect effect = new WolfGrowYewTreeEffect(msg -> {});

        long preCount = spawner.getExits().stream()
                .filter(e -> e.getDestination().getGround().getClass().equals(ProximityYewBerryTree.class))
                .count();

        effect.apply(newbornBear, spawner);

        long postCount = spawner.getExits().stream()
                .filter(e -> e.getDestination().getGround().getClass().equals(ProximityYewBerryTree.class))
                .count();

        assertEquals(preCount, postCount, "Non-wolf spawn should not grow a proximity yew tree.");
    }
}
