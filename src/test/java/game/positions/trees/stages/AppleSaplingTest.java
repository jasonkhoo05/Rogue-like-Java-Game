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

/**
 * Tests for AppleSapling stage (REQ1) — corrected to mock exits & destination Location.
 */
class AppleSaplingTest {

    private Location location;
    private GameMap map;

    @BeforeEach
    void setup() {
        location = mock(Location.class);
        map = mock(GameMap.class);
        when(location.map()).thenReturn(map);
    }

    @Test
    void saplingInForest_producesEveryTwoTurns_and_growsAfterFiveTurns() {
        when(map.toString()).thenReturn("Forest");

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

        // Make map.at(0,0) return destLoc so spawnApple will call destLoc.addItem(...)
        when(map.at(0, 0)).thenReturn(destLoc);

        AppleSapling sapling = new AppleSapling();

        // simulate 5 ticks (sapling should produce on ticks 2 and 4, and grow after tick 5)
        for (int i = 1; i <= 5; i++) {
            sapling.tick(location);
        }

        // verify fruits produced at least twice (turns 2 and 4)
        verify(destLoc, atLeast(2)).addItem(isA(game.items.Apple.class));

        // verify growth into AppleTree happens at/after the 5th tick (setGround invoked on location)
        verify(location, atLeastOnce()).setGround(isA(game.positions.trees.AppleTree.class));
    }

    @Test
    void saplingInForest_beforeGrowth_doesNotGrowEarly() {
        when(map.toString()).thenReturn("Forest");

        // stub exits to avoid spawn returning early (defensive)
        Exit exit = mock(Exit.class);
        Location destLoc = mock(Location.class);
        when(destLoc.x()).thenReturn(0);
        when(destLoc.y()).thenReturn(0);
        when(exit.getDestination()).thenReturn(destLoc);
        when(location.getExits()).thenReturn(Arrays.asList(exit));
        when(map.at(0,0)).thenReturn(destLoc);

        AppleSapling sapling = new AppleSapling();

        // 4 ticks -> not yet 5 ticks threshold
        for (int i = 1; i <= 4; i++) sapling.tick(location);

        verify(location, never()).setGround(isA(game.positions.trees.AppleTree.class));
    }

    @Test
    void saplingInUnknownMap_behavesSafely_noExceptions_and_maybeFruits() {
        when(map.toString()).thenReturn("MysteriousMap");

        // ensure exits are present so spawnApple can run if implementation decides to produce
        Exit exit = mock(Exit.class);
        Location destLoc = mock(Location.class);
        when(destLoc.x()).thenReturn(0);
        when(destLoc.y()).thenReturn(0);
        when(exit.getDestination()).thenReturn(destLoc);
        when(location.getExits()).thenReturn(Arrays.asList(exit));
        when(map.at(0,0)).thenReturn(destLoc);

        AppleSapling sapling = new AppleSapling();

        // Call several ticks to ensure no exceptions and observe behavior
        for (int i = 1; i <= 6; i++) sapling.tick(location);

        // Ensure it didn't crash; we don't assert exact addItem counts here because unknown map behaviour is undefined
        assertTrue(true, "Sapling ticked safely on unknown map (no exception thrown).");
    }
}
