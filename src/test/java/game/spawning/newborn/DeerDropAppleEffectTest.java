package game.spawning.newborn;

import edu.monash.fit2099.engine.GameEngineException;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.World;
import game.actors.Bear;
import game.actors.Deer;
import game.positions.Snow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for DeerDropAppleEffect:
 *  - When a DEER spawns, exactly one adjacent exit gains exactly one item.
 *  - Non-deer spawns do nothing.
 *  - A log line is emitted when applied to a deer.
 *
 * No instanceof checks are used: we assert by counting total items across exits.
 */
public class DeerDropAppleEffectTest {

    /** Minimal concrete World so GameMap actorLocations are wired for addActor if needed. */
    static class TestWorld extends World {
        public TestWorld(Display display) { super(display); }
        @Override protected String endGameMessage() { return "end"; }
    }

    private Display display;
    private TestWorld world;
    private DefaultGroundCreator gc;
    private GameMap map;

    @BeforeEach
    void setup() throws GameEngineException {
        display = new Display();
        world = new TestWorld(display);

        // Register an empty ground for '.'
        gc = new DefaultGroundCreator();
        gc.registerGround('.', Snow::new);

        // 3x3 map so the center has 8 exits
        map = new GameMap("TestMap", gc, Arrays.asList(
                "...",
                "...",
                "..."
        ));
        world.addGameMap(map);
    }

    private int countItemsOnExits(int cx, int cy) {
        return map.at(cx, cy).getExits().stream()
                .map(e -> e.getDestination().getItems().size())
                .reduce(0, Integer::sum);
    }

    private int[] itemsPerExit(int cx, int cy) {
        return map.at(cx, cy).getExits().stream()
                .mapToInt(e -> e.getDestination().getItems().size())
                .toArray();
    }

    @Test
    void deerSpawn_dropsExactlyOneItemOnExactlyOneExit_andLogs() throws GameEngineException {
        // Arrange
        var deer = new Deer("Deer", 'd', 50);
        var center = map.at(1, 1);
        // (optional) place deer on center – not strictly required for effect
        map.addActor(deer, center);

        StringBuilder log = new StringBuilder();
        var effect = new DeerDropAppleEffect(msg -> log.append(msg));

        int beforeTotal = countItemsOnExits(1, 1);
        int[] beforePerExit = itemsPerExit(1, 1);

        // Act
        effect.apply(deer, center);

        int afterTotal = countItemsOnExits(1, 1);
        int[] afterPerExit = itemsPerExit(1, 1);

        // Assert: total items across exits increased by exactly 1
        assertEquals(beforeTotal + 1, afterTotal, "Exactly one apple should be added on one exit.");

        // And exactly one exit index increased by 1
        int changed = 0;
        for (int i = 0; i < beforePerExit.length; i++) {
            int delta = afterPerExit[i] - beforePerExit[i];
            if (delta != 0) {
                assertEquals(1, delta, "The changed exit should gain exactly one item.");
                changed++;
            }
        }
        assertEquals(1, changed, "Exactly one exit should change.");

        // Log emitted
        assertTrue(log.toString().contains("[SPAWN]"), "A log line should be emitted.");
        assertTrue(log.toString().contains("Deer"), "Log should mention the deer.");
    }

    @Test
    void nonDeerSpawn_noDrop_noLog() {
        // Arrange
        var bear = new Bear("Bear", 'B', 200);
        var center = map.at(1, 1);

        StringBuilder log = new StringBuilder();
        var effect = new DeerDropAppleEffect(msg -> log.append(msg));

        int beforeTotal = countItemsOnExits(1, 1);

        // Act
        effect.apply(bear, center);

        int afterTotal = countItemsOnExits(1, 1);

        // Assert: nothing changed
        assertEquals(beforeTotal, afterTotal, "Non-deer should not drop anything.");
        assertEquals(0, log.length(), "No log should be emitted for non-deer.");
    }
}
