package game.spawning.newborn;

import edu.monash.fit2099.engine.GameEngineException;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.World;
import game.actors.Crocodile;
import game.actors.Player;
import game.positions.Snow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CrocodilePoisonAuraEffect:
 * - Poisons only actors in surrounding tiles (not the newborn on center).
 * - Deals 10 HP per tick for 3 ticks (we assert at least first tick here).
 * - Produces a readable log line including the target's name.
 */
public class CrocodilePoisonAuraEffectTest {

    /** Minimal concrete World so GameMap actorLocations are wired. */
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

        // Register at least '.' so GameMap can build
        gc = new DefaultGroundCreator();
        gc.registerGround('.', Snow::new);

        map = new GameMap("TestMap", gc, Arrays.asList(
                "...",
                "...",
                "..."
        ));
        world.addGameMap(map);
    }

    /** Helper to add a player with simple ctor defaults. */
    private Player makePlayer(String name, int hp) {
        return new Player(name, '@', hp, /*hydration*/50, /*warmth*/50, new java.util.ArrayList<>());
    }

    @Test
    void poisonsOnlySurroundings_notCenter() throws GameEngineException {
        // Arrange: center = croc (newborn), north = victim player
        var croc = new Crocodile("Croc", '<', 300);
        var victim = makePlayer("Victim", 100);

        map.addActor(croc, map.at(1, 1));   // spawner tile
        map.addActor(victim, map.at(1, 0)); // one of the surrounding tiles

        StringBuilder log = new StringBuilder();
        var effect = new CrocodilePoisonAuraEffect(3, 10, s -> log.append(s).append('\n'));

        int crocHPBefore = croc.getAttribute(edu.monash.fit2099.engine.actors.attributes.BaseAttributes.HEALTH);
        int victimHPBefore = victim.getAttribute(edu.monash.fit2099.engine.actors.attributes.BaseAttributes.HEALTH);

        // Act: apply aura and tick once (to process Poisoned status damage)
        effect.apply(croc, map.at(1, 1));
        map.tick(); // triggers tickStatus on actors -> applies 10 dmg to poisoned targets

        int crocHPAfter = croc.getAttribute(edu.monash.fit2099.engine.actors.attributes.BaseAttributes.HEALTH);
        int victimHPAfter = victim.getAttribute(edu.monash.fit2099.engine.actors.attributes.BaseAttributes.HEALTH);

        // Assert
        assertEquals(crocHPBefore, crocHPAfter, "Crocodile on spawner tile must NOT be poisoned.");
        assertEquals(victimHPBefore - 10, victimHPAfter, "Victim on surrounding tile should lose 10 HP after 1 tick.");

        String logStr = log.toString();
        assertTrue(logStr.contains("[CROCODILE AOE]"), "Log should contain AOE prefix.");

    }

    @Test
    void noActorsNearby_noPoisonApplied() throws GameEngineException {
        var croc = new Crocodile("Croc", '<', 300);
        map.addActor(croc, map.at(1, 1));

        var effect = new CrocodilePoisonAuraEffect(3, 10, s -> {});

        int crocHPBefore = croc.getAttribute(edu.monash.fit2099.engine.actors.attributes.BaseAttributes.HEALTH);
        effect.apply(croc, map.at(1, 1));
        map.tick(); // nothing should happen

        int crocHPAfter = croc.getAttribute(edu.monash.fit2099.engine.actors.attributes.BaseAttributes.HEALTH);
        assertEquals(crocHPBefore, crocHPAfter, "No neighbors → croc shouldn't be damaged.");
    }

    @Test
    void multipleNeighbors_allArePoisoned() throws GameEngineException {
        var croc = new Crocodile("Croc", '<', 300);
        var p1 = makePlayer("P1", 60);
        var p2 = makePlayer("P2", 70);

        map.addActor(croc, map.at(1, 1));
        map.addActor(p1,  map.at(0, 1)); // west
        map.addActor(p2,  map.at(2, 1)); // east

        var effect = new CrocodilePoisonAuraEffect(3, 10, s -> {});

        int p1Before = p1.getAttribute(edu.monash.fit2099.engine.actors.attributes.BaseAttributes.HEALTH);
        int p2Before = p2.getAttribute(edu.monash.fit2099.engine.actors.attributes.BaseAttributes.HEALTH);

        effect.apply(croc, map.at(1, 1));
        map.tick();

        int p1After = p1.getAttribute(edu.monash.fit2099.engine.actors.attributes.BaseAttributes.HEALTH);
        int p2After = p2.getAttribute(edu.monash.fit2099.engine.actors.attributes.BaseAttributes.HEALTH);

        assertEquals(p1Before - 10, p1After, "P1 should be poisoned for 10 dmg.");
        assertEquals(p2Before - 10, p2After, "P2 should be poisoned for 10 dmg.");
    }
}
