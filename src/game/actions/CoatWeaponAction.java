package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import game.items.weapons.AbstractCoatableWeapon;
import game.items.weapons.coating.Coating;

/**
 * Apply a coating to a specific coatable weapon.
 * Each coatable weapon will show as a separate menu option.
 * If sourceItem != null, it will be consumed (e.g., Yewberry).
 */
public class CoatWeaponAction extends Action {
    private final Coating coating;
    private final Item sourceItem;                // null if from snow
    private final AbstractCoatableWeapon target;  // weapon chosen to coat

    public CoatWeaponAction(Coating coating, Item sourceItem, AbstractCoatableWeapon target) {
        this.coating = coating;
        this.sourceItem = sourceItem;
        this.target = target;
    }

    @Override
    public String execute(Actor actor, GameMap map) {
        target.coatWith(coating);

        if (sourceItem != null) {
            actor.removeItemFromInventory(sourceItem);
        }

        return actor + " coats " + target + " with " + coating.name() + ".";
    }

    @Override
    public String menuDescription(Actor actor) {
        return "Coat " + target + " with " + coating.name();
    }

    /**
     * Generate a list of coating actions for all coatable weapons.
     * Used by YewBerry and Snow to add menu entries dynamically.
     */
    public static ActionList buildFor(Actor actor, Coating coating, Item sourceItem) {
        ActionList list = new ActionList();
        for (Item it : actor.getItemInventory()) {
            if (it instanceof AbstractCoatableWeapon w && w.isCoatable()) {
                list.add(new CoatWeaponAction(coating, sourceItem, w));
            }
        }
        return list;
    }
}

