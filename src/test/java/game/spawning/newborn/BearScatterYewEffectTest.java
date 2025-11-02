package game.spawning.newborn;

import edu.monash.fit2099.engine.GameEngineException;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.positions.World;
import game.actors.Bear;
import game.actors.Deer;
import game.positions.Snow; // '.' ground mapping
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/** Tests for BearScatterYewEffect. */
public class BearScatterYewEffectTest {

    /** Minimal concrete World (so GameMap is fully initialised). */
    private static class TestWorld extends World {
        public TestWorld(Display display) { super(display); }
        @Override protected String endGameMessage() { return "end"; }
    }

    private GameMap map;
    private Location center;
    private Display display;

    @BeforeEach
    public void setup() throws Exception {
        display = new Display();

        // Register '.' so ASCII map creation works
        DefaultGroundCreator gc = new DefaultGroundCreator();
        gc.registerGround('.', Snow::new);

        // 5x5 map so the center has all 8 exits
        map = new GameMap("TestMap", gc, List.of(
                ".....",
                ".....",
                ".....",
                ".....",
                "....."
        ));

        // Put map in a world so actor/location plumbing is complete
        TestWorld world = new TestWorld(display);
        world.addGameMap(map);

        center = map.at(2, 2); // center cell has 8 exits
    }

    private int countYewAround(Location loc) {
        return (int) loc.getExits().stream()
                .map(e -> e.getDestination().getItems())
                .flatMap(List::stream)
                .filter(it -> it.getClass().getSimpleName().equals("YewBerry"))
                .count();
    }

    private int[] yewPerExit(Location loc) {
        return loc.getExits().stream()
                .mapToInt(e -> (int) e.getDestination().getItems().stream()
                        .filter(it -> it.getClass().getSimpleName().equals("YewBerry"))
                        .count())
                .toArray();
    }

    @Test
    public void chanceZero_noDrops_andOneSummaryLog() {
        List<String> logs = new ArrayList<>();
        BearScatterYewEffect effect = new BearScatterYewEffect(0.0, logs::add);

        // Newborn is a Bear → eligible, but chance=0 blocks drops
        effect.apply(new Bear("Bear", 'B', 200), center);

        assertEquals(0, countYewAround(center), "With chance=0, no YewBerry should drop on any exit");
        assertEquals(1, logs.size(), "Should log the single 'no drop' summary");
        assertTrue(logs.get(0).contains("no YewBerry dropped"), "Summary message should indicate no drop");
    }

    @Test
    public void chanceOne_dropsOnAllExits_andEightLogs() {
        List<String> logs = new ArrayList<>();
        BearScatterYewEffect effect = new BearScatterYewEffect(1.0, logs::add);

        effect.apply(new Bear("Bear", 'B', 200), center);

        // Center has 8 exits in this engine
        assertEquals(8, countYewAround(center), "With chance=1.0, one YewBerry should drop on every exit");
        assertEquals(8, logs.size(), "Should log once per dropped berry (8 exits → 8 logs)");

        // Also confirm exactly one per exit
        for (int c : yewPerExit(center)) {
            assertEquals(1, c, "Each exit should get exactly one YewBerry when chance=1.0");
        }
    }

    @Test
    public void nonBear_noEffectEvenWithChanceOne() {
        List<String> logs = new ArrayList<>();
        BearScatterYewEffect effect = new BearScatterYewEffect(1.0, logs::add);

        // Newborn is NOT a bear -> effect should do nothing
        effect.apply(new Deer("Deer", 'd', 50), center);

        assertEquals(0, countYewAround(center), "Non-bear newborn should not trigger any YewBerry drops");
        assertEquals(0, logs.size(), "No logs expected when effect does not apply");
    }

    @Test
    public void chanceClampedWithinBounds() {
        // Even if someone passes >1 or <0, ctor clamps; but here we just ensure normal construction works
        assertDoesNotThrow(() -> new BearScatterYewEffect(2.0, s -> {}));
        assertDoesNotThrow(() -> new BearScatterYewEffect(-0.5, s -> {}));
    }
}
