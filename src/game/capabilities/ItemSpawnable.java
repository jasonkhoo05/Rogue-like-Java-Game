package game.capabilities;

import edu.monash.fit2099.engine.positions.Location;

import java.util.Random;

/**
 * Represents trees that can spawn items
 */
public interface ItemSpawnable {
    void spawn(Location location);
    Random random = new Random();
}
