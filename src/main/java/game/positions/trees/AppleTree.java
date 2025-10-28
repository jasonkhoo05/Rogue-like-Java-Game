package game.positions.trees;

import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import game.Ability;
import game.capabilities.ItemSpawnable;
import game.items.Apple;

import java.util.List;

public class AppleTree extends Tree implements ItemSpawnable {

    /**
     * Constructor
     */
    public AppleTree() {
        super('T', "Apple Tree");
        this.enableAbility(Ability.ITEMSPAWNABLE);
    }

    /**
     * Ground experiencing the flow of time.
     *
     * @param location The location of the Ground
     */
    @Override
    public void tick(Location location) {
        super.tick(location);
        // When starting the game, it already counts as one turn
        spawn(location);
    }

    /**
     * Spawns new item around its surrounding
     * @param location the location of this
     */
    public void spawn(Location location) {
        if (this.counter % 3 == 0) {

            List<Exit> listOfExits = location.getExits();
            Exit chosenExit = listOfExits.get(ItemSpawnable.random.nextInt(listOfExits.size()));
            Location exitDestination = chosenExit.getDestination();
            int xCoor = exitDestination.x();
            int yCoor = exitDestination.y();
            location.map().at(xCoor, yCoor).addItem(new Apple());

        }
    }

}
