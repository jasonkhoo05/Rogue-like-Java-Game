package game.actors;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperation;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttribute;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.displays.Menu;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import game.Ability;
import game.StatusEffects;
import game.actions.AttackAction;
import game.actors.attributes.PlayerAttribute;
import game.system.FireSystem;
import game.weapons.BareFist;

import java.util.ArrayList;

/**
 * Class representing the Player.
 * @author Adrian Kristanto
 */
public class Player extends Actor {
    /**
     * Constructor.
     *
     * @param name        Name to call the player in the UI
     * @param displayChar Character to represent the player in the UI
     * @param hitPoints   Player's starting number of hit points
     */
    public Player(String name, char displayChar, int hitPoints, int hydrationLevel, int warmthLevel, ArrayList<Item> items) {
        super(name, displayChar, hitPoints);
        this.setIntrinsicWeapon(new BareFist());

        this.addNewStatistic(PlayerAttribute.HYDRATION, new BaseActorAttribute(9999));
        this.addNewStatistic(PlayerAttribute.WARMTH, new BaseActorAttribute(9999));

        this.modifyAttribute(PlayerAttribute.HYDRATION, ActorAttributeOperation.UPDATE, hydrationLevel);
        this.modifyAttribute(PlayerAttribute.WARMTH, ActorAttributeOperation.UPDATE, warmthLevel);

        for (Item item : items){
            this.addItemToInventory(item);
        }

        this.enableAbility(Ability.CAN_ATTACK);
    }

    /**
     * Select and return an action to perform on the current turn.
     *
     * @param actions collection of possible Actions for this Actor
     * @param lastAction The Action this Actor took last turn. Can do
     * interesting things in conjunction with Action.getNextAction()
     * @param map the map containing the Actor
     * @param display the I/O object to which messages may be written
     * @return the Action to be performed
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        StatusEffects.tick(this, map);
        FireSystem.tick(map);
        // Handle multi-turn Actions
        if (lastAction.getNextAction() != null)
            return lastAction.getNextAction();

        this.modifyAttribute(PlayerAttribute.HYDRATION, ActorAttributeOperation.DECREASE, 1);
        this.modifyAttribute(PlayerAttribute.WARMTH, ActorAttributeOperation.DECREASE, 1);

        display.println(this.toString());
        display.println("HYDRATION: " + this.getAttribute(PlayerAttribute.HYDRATION));
        display.println("WARMTH: " + this.getAttribute(PlayerAttribute.WARMTH));

        // return/print the console menu
        Menu menu = new Menu(actions);
        return menu.showMenu(this, display);
    }

    /**
     * Checks if player is still conscious
     * @return true if hydrationLevel > 1 and warmthLevel > 1; false otherwise
     */
    public boolean isConscious(){
        return super.isConscious() && this.getAttribute(PlayerAttribute.HYDRATION) > 1 && this.getAttribute(PlayerAttribute.WARMTH) > 1;
    }

    /**
     * Returns a new collection of the Actions that the otherActor can do to the
     * current Actor.
     *
     * @param otherActor the Actor that might be performing attack
     * @param direction String representing the direction of the other Actor
     * @param map current GameMap
     * @return A collection of Actions.
     */
    @Override
    public ActionList allowableActions(Actor otherActor, String direction, GameMap map) {
        ActionList actions = new ActionList();
        if(otherActor.hasAbility(Ability.CAN_ATTACK)){
            actions.add(new AttackAction(this, direction));
        }
        return actions;
    }
}