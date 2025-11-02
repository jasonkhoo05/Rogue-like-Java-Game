package game.positions.spawners;

import edu.monash.fit2099.engine.GameEngineException;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.positions.World;
import game.actors.Crocodile;
import game.actors.Deer;
import game.actors.Player;
import game.capabilities.BehaviourHost;
import game.positions.Snow; // for '.'
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SwampTest {

    /** Minimal concrete World so GameMap gets actorLocations wired up. */
    private static class TestWorld extends World {
        public TestWorld(Display display) { super(display); }
        @Override protected String endGameMessage() { return "test over"; }
    }

    private TestWorld world;
    private GameMap map;
    private Display display;

    @BeforeEach
    public void setup() throws Exception {
        display = new Display();

        // Register '.' so GameMap constructor can create ground from ASCII.
        DefaultGroundCreator groundCreator = new DefaultGroundCreator();
        groundCreator.registerGround('.', Snow::new);

        map = new GameMap("TestMap", groundCreator, List.of(
                ".....",
                ".....",
                ".....",
                ".....",
                "....."
        ));

        // Put map into a concrete World
        world = new TestWorld(display);
        world.addGameMap(map);
    }

    private void tickLocation(Location loc) {
        // Only tick this tile's ground (spawner runs here)
        loc.tick();
    }

    @Test
    public void noSpawnWhenNoNearbyActors() {
        Location swampLoc = map.at(2, 2);
        Swamp swamp = new Swamp(); // 50% chance but gated by nearby-actor
        swamp.addFactory(() -> new Deer("Deer", 'd', 50));
        swampLoc.setGround(swamp);

        for (int i = 0; i < 40; i++) {
            tickLocation(swampLoc);
            assertFalse(swampLoc.containsAnActor(),
                    "Swamp should NOT spawn without nearby actors (tick " + i + ")");
        }
    }

    @Test
    public void spawnsWhenNearbyActorEventually_andNewbornIsAnimal() throws GameEngineException {
        Location swampLoc = map.at(2, 2);
        Swamp swamp = new Swamp();
        swamp
                .addFactory(() -> new Crocodile("Croc", '<', 300))
                .addFactory(() -> new Deer("Deer", 'd', 50));
        swampLoc.setGround(swamp);

        // Place a player adjacent so the gate condition is true.
        Actor player = new Player("Tester", '@', 100, 50, 50, new ArrayList<>());
        map.addActor(player, map.at(2, 1)); // north

        boolean spawned = false;
        for (int i = 0; i < 200; i++) {
            tickLocation(swampLoc);
            if (swampLoc.containsAnActor()) { spawned = true; break; }
        }
        assertTrue(spawned, "Swamp should eventually spawn when a nearby actor exists.");

        Actor newborn = map.getActorAt(swampLoc);
        assertNotNull(newborn, "Expected a newborn on the swamp tile after spawn.");
        assertTrue(newborn.asCapability(BehaviourHost.class).isPresent(),
                "Newborn should expose BehaviourHost capability (animal) — no instanceof needed.");
    }

    @Test
    public void newbornFromSwampGetsPoisoned_effectVisibleAsHPDrop() throws GameEngineException {
        Location swampLoc = map.at(2, 2);
        Swamp swamp = new Swamp(); // includes PoisonOnSpawnEffect(10, 5)
        swamp.addFactory(() -> new Deer("Deer", 'd', 60));
        swampLoc.setGround(swamp);

        // Satisfy nearby-actor gate
        Actor player = new Player("Tester", '@', 100, 50, 50, new ArrayList<>());
        map.addActor(player, map.at(1, 2)); // west

        // Wait until something spawns
        for (int i = 0; i < 200 && !swampLoc.containsAnActor(); i++) {
            tickLocation(swampLoc);
        }
        assertTrue(swampLoc.containsAnActor(), "Expected a deer to spawn from the swamp.");

        Actor newborn = map.getActorAt(swampLoc);
        assertNotNull(newborn);

        int hpBefore = newborn.getAttribute(
                edu.monash.fit2099.engine.actors.attributes.BaseAttributes.HEALTH);

        // Use full map ticks so statuses are processed
        map.tick(); // poison turn 1
        map.tick(); // poison turn 2

        int hpAfter = newborn.getAttribute(
                edu.monash.fit2099.engine.actors.attributes.BaseAttributes.HEALTH);

        assertTrue(hpAfter < hpBefore,
                "Poison should reduce HP after ticks. Before=" + hpBefore + ", After=" + hpAfter);
    }
}
