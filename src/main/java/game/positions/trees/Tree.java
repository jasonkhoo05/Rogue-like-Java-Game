package game.positions.trees;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;

public abstract class Tree extends Ground {
    int counter;
    /**
     * Constructor.
     *
     * @param displayChar character to display for this type of terrain
     * @param name name of the tree
     */
    public Tree(char displayChar, String name) {
        super(displayChar, name);
    }

    /**
     * Implements impassable terrain, or terrain that is only
     * passable if conditions are met.
     *
     * @param actor the Actor to check
     * @return true
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        return false;
    }

    /**
     * Ground experiencing the flow of time.
     *
     * @param location The location of the Ground
     */
    @Override
    public void tick(Location location) {
        counter += 1;
    }

    protected int age() {
        return counter;
    }

    protected boolean every(int n) {
        return n > 0 && (counter % n == 0);
    }
}
