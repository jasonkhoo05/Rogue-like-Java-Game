package game.positions.trees;

import edu.monash.fit2099.engine.GameEngineException;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.positions.World;
import game.actors.Player;
import game.items.YewBerry;
import game.positions.Snow; // for '.'
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProximityYewBerryTreeTest {

    /** Minimal concrete World so GameMap gets actorLocations wired. */
    private static class TestWorld extends World {
        public TestWorld(Display display) { super(display); }
        @Override protected String endGameMessage() { return "test over"; }
    }

    private TestWorld world;
    private GameMap map;
    private Display display;

    @BeforeEach
    void setup() throws Exception {
        display = new Display();
        DefaultGroundCreator groundCreator = new DefaultGroundCreator();
        // Register '.' so ASCII map works
        groundCreator.registerGround('.', Snow::new);

        map = new GameMap("TestMap", groundCreator, List.of(
                ".....",
                ".....",
                ".....",
                ".....",
                "....."
        ));

        world = new TestWorld(display);
        world.addGameMap(map);
    }

    private int countYewBerries(Location loc) {
        int c = 0;
        for (Item it : loc.getItems()) {
            if (it instanceof YewBerry) c++;
        }
        return c;
    }

    @Test
    void noPeriodicDropWhenNoActorsNearby() {
        Location treeLoc = map.at(2, 2);
        treeLoc.setGround(new ProximityYewBerryTree());

        // Tick the map multiple times; should NOT drop without nearby actors.
        for (int i = 0; i < 10; i++) map.tick();

        // Check all adjacent tiles have zero YewBerry
        for (var e : treeLoc.getExits()) {
            assertEquals(0, countYewBerries(e.getDestination()),
                    "No proximity drop should occur without nearby actors.");
        }
    }

    @Test
    void dropsOneBerryPerTickWhenActorAdjacent() throws GameEngineException {
        Location treeLoc = map.at(2, 2);
        treeLoc.setGround(new ProximityYewBerryTree());

        // Place player adjacent (north)
        Actor player = new Player("Tester", '@', 100, 50, 50, new ArrayList<>());
        map.addActor(player, map.at(2, 1));

        // Tick 3 times → expect exactly 3 total berries across adjacent tiles
        int ticks = 3;
        for (int i = 0; i < ticks; i++) map.tick();

        int totalAdjacentBerries = treeLoc.getExits().stream()
                .map(e -> countYewBerries(e.getDestination()))
                .reduce(0, Integer::sum);

        assertEquals(ticks, totalAdjacentBerries,
                "Should drop exactly one YewBerry per tick while an actor is nearby.");
    }

    @Test
    void dropsOnRandomAdjacentTileOnlyOncePerTick() throws GameEngineException {
        Location treeLoc = map.at(2, 2);
        treeLoc.setGround(new ProximityYewBerryTree());

        // Put an actor adjacent to trigger proximity
        Actor player = new Player("Tester", '@', 100, 50, 50, new ArrayList<>());
        map.addActor(player, map.at(3, 2)); // east

        // One tick → exactly 1 berry placed across all exits
        map.tick();

        int sum = treeLoc.getExits().stream()
                .map(e -> countYewBerries(e.getDestination()))
                .reduce(0, Integer::sum);

        assertEquals(1, sum, "Exactly one YewBerry should drop per tick.");
    }
}
