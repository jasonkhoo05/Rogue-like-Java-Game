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
 * Tests for AppleSprout stage (REQ1) — corrected to mock exits & use GROW_AFTER=3.
 */
class AppleSproutTest {

    private Location location;
    private GameMap map;

    @BeforeEach
    void setup() {
        location = mock(Location.class);
        map = mock(GameMap.class);
        when(location.map()).thenReturn(map);
    }

    @Test
    void forestSprout_beforeThreeTicks_doesNotGrow() {
        when(map.toString()).thenReturn("Forest");

        AppleSprout sprout = new AppleSprout();

        // 2 ticks: should not have grown yet (since GROW_AFTER = 3)
        sprout.tick(location);
        sprout.tick(location);

        // verify no transition to sapling or tree
        verify(location, never()).setGround(isA(AppleSapling.class));
        verify(location, never()).setGround(isA(game.positions.trees.AppleTree.class));
    }

    @Test
    void forestSprout_afterThreeTicks_growsToSapling() {
        when(map.toString()).thenReturn("Forest");

        AppleSprout sprout = new AppleSprout();

        // simulate exactly 3 turns
        sprout.tick(location);
        sprout.tick(location);
        sprout.tick(location);

        // should transition to AppleSapling at or after 3rd tick
        verify(location, atLeastOnce()).setGround(isA(AppleSapling.class));
    }

    @Test
    void plainsSprout_afterThreeTicks_skipsSapling_and_growsToTree_and_producesApplesEveryTick() {
        when(map.toString()).thenReturn("Plains");

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

        AppleSprout sprout = new AppleSprout();

        // On Plains, sprouts produce an apple every turn and become a tree after 3 turns
        sprout.tick(location); // tick 1 -> should spawn apple
        sprout.tick(location); // tick 2 -> spawn apple
        sprout.tick(location); // tick 3 -> should become AppleTree and also spawn apple for tick 3

        // verify apples spawned at least 3 times (turns 1,2,3)
        verify(destLoc, atLeast(3)).addItem(isA(game.items.Apple.class));

        // verify it became a tree (skipped sapling)
        verify(location, atLeastOnce()).setGround(isA(game.positions.trees.AppleTree.class));
        verify(location, never()).setGround(isA(AppleSapling.class));
    }
}
