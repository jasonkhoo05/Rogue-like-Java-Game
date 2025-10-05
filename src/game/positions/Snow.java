package game.positions;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;

/**
 * A class representing snow on the ground.
 * @author Adrian Kristanto
 */
public class Snow extends Ground {
    public Snow() {
        super('.', "Snow");
    }

    @Override
    public ActionList allowableActions(Actor actor, Location location, String direction) {
        ActionList list = new ActionList();

        // Standing on Snow: Adds a "Snow Coating" menu item for each coatable weapon in your inventory (does not consume the item)
        for (var a : game.actions.CoatWeaponAction.buildFor(
                actor,
                new game.items.weapons.coating.SnowCoating(),
                null // The source is not an item, so it is not consumed.
        )) {
            list.add(a);
        }
        return list;
    }
}
