package game.actors;

import ai.Monologue;
import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperation;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttribute;
import edu.monash.fit2099.engine.actors.attributes.BaseAttributes;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.displays.Menu;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.weapons.IntrinsicWeapon;
import game.Ability;
import game.status.DecreaseWarmth;
import game.status.StatusEffects;
import game.actions.AttackAction;
import game.actors.attributes.PlayerAttribute;
import game.behaviours.HealBehaviour;
import game.capabilities.Dehydratable;
import game.weapons.BareFist;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.weapons.Weapon;
import game.actions.RangedAttackAction;
import game.weapons.PoweredBareFist;

import java.util.ArrayList;

/**
 * Class representing the Player.
 * @author Adrian Kristanto
 */
public class Player extends Actor implements Dehydratable {

    private final Monologue monologueAI;
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

        this.addNewStatistic(PlayerAttribute.HYDRATION, new BaseActorAttribute(99));
        this.addNewStatistic(PlayerAttribute.WARMTH, new BaseActorAttribute(99));

        this.modifyAttribute(PlayerAttribute.HYDRATION, ActorAttributeOperation.UPDATE, hydrationLevel);
        this.modifyAttribute(PlayerAttribute.WARMTH, ActorAttributeOperation.UPDATE, warmthLevel);

        for (Item item : items){
            this.addItemToInventory(item);
        }

        this.enableAbility(Ability.CAN_ATTACK);
        this.addStatus(new DecreaseWarmth());
        this.enableAbility(Ability.HEALABLE);
        this.enableAbility(Ability.IS_PLAYER);

        this.monologueAI = new Monologue();
    }

    /**
     * Select and return an action to perform on the current turn.
     *
     * @param actions    collection of possible Actions for this Actor
     * @param lastAction The Action this Actor took last turn. Can do
     *                   interesting things in conjunction with Action.getNextAction()
     * @param map        the map containing the Actor
     * @param display    the I/O object to which messages may be written
     * @return the Action to be performed
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        StatusEffects.tick(this, map);

        // Handle multi-turn Actions
        if (lastAction.getNextAction() != null)
            return lastAction.getNextAction();

        this.modifyAttribute(PlayerAttribute.HYDRATION, ActorAttributeOperation.DECREASE, 1);

        addRangedAttackOptions(actions, map);
        display.println(this.toString());
        display.println("HYDRATION: " + this.getAttribute(PlayerAttribute.HYDRATION));
        display.println("WARMTH: " + this.getAttribute(PlayerAttribute.WARMTH));

        performMonologue();

        // return/print the console menu
        Menu menu = new Menu(actions);
        return menu.showMenu(this, display);
    }


    /** Add attack options for ranged weapons such as bows to the menu;
     * does not rely on adjacent allowableActions calls */
    private void addRangedAttackOptions(ActionList actions, GameMap map) {
        // Range is set to 3
        final int MAX_RANGE = 3;

        // If the player has no attack ability, return directly
        if (!this.hasAbility(Ability.CAN_ATTACK)) return;

        // Find player coordinates
        Location me = map.locationOf(this);

        for (Item it : this.getItemInventory()) {
            var maybeWeapon = it.asCapability(Weapon.class);
            if (maybeWeapon.isEmpty()) continue;

            // Must be marked with the "Ranged Weapon"
            if (!it.hasAbility(Ability.RANGED_WEAPON)) continue;

            Weapon weapon = maybeWeapon.get();

            // Scans a square area of MAX_RANGE around the player
            for (int dx = -MAX_RANGE; dx <= MAX_RANGE; dx++) {
                for (int dy = -MAX_RANGE; dy <= MAX_RANGE; dy++) {
                    if (dx == 0 && dy == 0) continue;         // skip self
                    int x = me.x() + dx, y = me.y() + dy;

                    if (!map.getXRange().contains(x) || !map.getYRange().contains(y)) continue;

                    Location loc = map.at(x, y);
                    if (!map.isAnActorAt(loc)) continue;

                    Actor target = map.getActorAt(loc);

                    int dist = Math.max(Math.abs(dx), Math.abs(dy));
                    if (dist > 1 && dist <= MAX_RANGE) {
                        actions.add(new RangedAttackAction(target, weapon, MAX_RANGE));
                    }
                }
            }
        }
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

    @Override
    public void hurt(int damage) {
        // If the actor is immune (from Aegis), skip damage
        if (this.hasAbility(Ability.IMMUNITY)) {
            System.out.println(this + " is shielded by Aegis and takes no damage!");
            return;
        }
        this.modifyAttribute(BaseAttributes.HEALTH, ActorAttributeOperation.DECREASE, damage);
    }


    @Override
    public IntrinsicWeapon getIntrinsicWeapon() {
        IntrinsicWeapon weapon;
        if (this.hasAbility(Ability.BOOST_DAMAGE)) {
            weapon = new PoweredBareFist();
        } else {
            weapon = new BareFist();
        }

        // DEBUG: print the type of weapon being returned
        System.out.println(this + " intrinsic weapon is: " + weapon.getClass().getSimpleName());

        return weapon;
    }

    @Override
    public void dehydrate(int hydrationValue) {
        this.modifyAttribute(PlayerAttribute.HYDRATION, ActorAttributeOperation.DECREASE, hydrationValue);
    }

    public void performMonologue() {
        String prompt = "I am in a jungle,display one monologue an npc would say of how his day going (first person view)";
        String text = monologueAI.generate(prompt);
        System.out.println(this + " says: " + text);
    }

}
