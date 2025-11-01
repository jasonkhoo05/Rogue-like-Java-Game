package game.positions.trees.stages;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Exit;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Tests for YewBerrySapling (REQ1) — covers probabilistic growth and map-specific fruiting.
 */
class YewBerrySaplingTest {

    private Location location;
    private GameMap map;

    @BeforeEach
    void setup() {
        location = mock(Location.class);
        map = mock(GameMap.class);
        when(location.map()).thenReturn(map);
    }

    @Test
    void yewSapling_grows_ifRandomIndicatesGrowth_afterThreeTicks() {
        when(map.toString()).thenReturn("Forest");

        // deterministic Random stub that always yields < 0.5 (growth)
        Random deterministic = mock(Random.class);
        when(deterministic.nextDouble()).thenReturn(0.1);

        YewBerrySapling sapling = new YewBerrySapling(deterministic);

        sapling.tick(location);
        sapling.tick(location);
        sapling.tick(location); // after 3 ticks, attempt to grow

        verify(location, atLeastOnce()).setGround(isA(game.positions.trees.YewBerryTree.class));
    }

    @Test
    void yewSapling_doesNotGrow_ifRandomIndicatesNoGrowth_afterThreeTicks() {
        when(map.toString()).thenReturn("Forest");

        Random deterministic = mock(Random.class);
        when(deterministic.nextDouble()).thenReturn(0.9); // > 0.5 so no growth

        YewBerrySapling sapling = new YewBerrySapling(deterministic);

        sapling.tick(location);
        sapling.tick(location);
        sapling.tick(location);

        verify(location, never()).setGround(isA(game.positions.trees.YewBerryTree.class));
    }

    @Test
    void plainsSapling_producesYewberries_everyTwoTurns_but_forestSaplingDoesNot() {
        // Prepare a mocked destination Location which will receive addItem(...)
        Location destLoc = mock(Location.class);
        when(destLoc.x()).thenReturn(0);
        when(destLoc.y()).thenReturn(0);

        // Prepare an Exit whose destination is destLoc
        Exit exit = mock(Exit.class);
        when(exit.getDestination()).thenReturn(destLoc);

        // Make location.getExits() return a list containing that exit
        List<Exit> exits = Arrays.asList(exit);
        when(location.getExits()).thenReturn(exits);

        // Make map.at(0,0) return destLoc so spawnYewBerry will call destLoc.addItem(...)
        when(map.at(0, 0)).thenReturn(destLoc);

        // Plains behavior
        when(map.toString()).thenReturn("Plains");
        Random deterministic = mock(Random.class);
        when(deterministic.nextDouble()).thenReturn(0.9); // avoid growth during this test

        YewBerrySapling plainsSap = new YewBerrySapling(deterministic);

        // simulate 4 ticks => should produce on ticks 2 and 4 on Plains
        for (int i = 0; i < 4; i++) plainsSap.tick(location);

        // verify addItem was called on the destination Location at least twice
        verify(destLoc, atLeast(2)).addItem(isA(game.items.YewBerry.class));

        // Forest behavior: should not produce while sapling
        reset(destLoc, location, map); // clear previous interactions & stubs

        // Re-stub after reset for forest case
        when(location.map()).thenReturn(map);
        when(location.getExits()).thenReturn(exits);
        when(exit.getDestination()).thenReturn(destLoc);
        when(map.at(0, 0)).thenReturn(destLoc);
        when(map.toString()).thenReturn("Forest");

        YewBerrySapling forestSap = new YewBerrySapling(deterministic);

        for (int i = 0; i < 4; i++) forestSap.tick(location);

        // verify destination did not receive any yewberry while sapling on Forest
        verify(destLoc, never()).addItem(isA(game.items.YewBerry.class));
    }
}
