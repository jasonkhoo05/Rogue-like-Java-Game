package game.actors;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.Behaviour;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperation;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttribute;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.weapons.Weapon;
import game.Ability;
import game.actions.RangedAttackAction;
import game.status.StatusEffects;
import game.actions.AttackAction;
import game.actions.NaturalDeathAction;
import game.actions.TameAction;
import game.actors.attributes.AnimalAttribute;
import game.capabilities.BehaviourHost;
import game.capabilities.Tameable;
import game.status.DecreaseWarmth;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public abstract class Animal extends Actor implements Tameable, BehaviourHost {
    Map<Integer, Behaviour> behaviours = new TreeMap<>();

    @Override
    public <T> Optional<T> asCapability(Class<T> capability) {
        if (capability == BehaviourHost.class) {
            return Optional.of(capability.cast(this));
        }
        return super.asCapability(capability);
    }

    /**
     * The constructor of the Actor class.
     *
     * @param name        the name of the Actor
     * @param displayChar the character that will represent the Actor in the
     *                    display
     * @param hitPoints   the Actor's starting hit points
     */
    public Animal(String name, char displayChar, int hitPoints, int startingWarmth) {
        super(name, displayChar, hitPoints);

        // Warmth attribute (cap 99 is sufficient; adjust if you want)
        this.addNewStatistic(AnimalAttribute.WARMTH, new BaseActorAttribute(99));
        this.modifyAttribute(AnimalAttribute.WARMTH, ActorAttributeOperation.UPDATE, startingWarmth);

        // Attach the warmth decay status (sets HEALTH=0 when warmth hits 0)
        this.addStatus(new DecreaseWarmth());
    }

    /** Optional: print a compact stat line each turn. */
    protected void printStats(GameMap map, Display display) {

        int warmth = getAttribute(AnimalAttribute.WARMTH);

        display.println(String.format("[%s %c WARMTH=%d]",
                this, this.getDisplayChar(),warmth));
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
        // Resolving ongoing effects
        StatusEffects.tick(this, map);

        // If unconscious (e.g., warmth reached 0), announce via engine and remove
        if (!this.isConscious()) {
            return new NaturalDeathAction();
        }

        // (Optional) Show stats
        printStats(map, display);


        for (Behaviour behaviour : behaviours.values()) {
            Action action = behaviour.generateAction(this, map);
            if(action != null)
                return action;
        }
        return new DoNothingAction();
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

        if (otherActor.hasAbility(Ability.CAN_ATTACK)) {
            Location a = map.locationOf(otherActor);
            Location t = map.locationOf(this);
            int dist = Math.max(Math.abs(a.x() - t.x()), Math.abs(a.y() - t.y()));
            for (Item it : otherActor.getItemInventory()) {
                Optional<Weapon> maybeWeapon =
                        it.asCapability(Weapon.class);

                if (maybeWeapon.isEmpty()) continue;
                Weapon w = maybeWeapon.get();
                if (dist == 1) {
                    actions.add(new AttackAction(this, direction, w));
                }

                if (it.hasAbility(Ability.RANGED_WEAPON)) {
                    final int BOW_RANGE = 3;
                    if (dist > 1 && dist <= BOW_RANGE) {
                        actions.add(new RangedAttackAction(this, w, BOW_RANGE));
                    }
                }
            }
            if (dist == 1) {
                actions.add(new AttackAction(this, direction));
            }
        }

        if (!this.hasAbility(Ability.TAMED)) {
            for (Item item : otherActor.getItemInventory()) {
                if (item.hasAbility(Ability.CAN_TAME)) {
                    actions.add(new TameAction(this, item));
                }
            }
        }

        return actions;
    }

    @Override
    public void putBehaviour(int priority, Behaviour behaviour) {
        behaviours.put(priority, behaviour);
    }
}

