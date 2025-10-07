package game.actors;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.Behaviour;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.weapons.Weapon;
import game.Ability;
import game.StatusEffects;
import game.actions.AttackAction;
import game.actions.TameAction;
import game.behaviours.FightAlongSideBehaviour;
import game.behaviours.FollowBehaviour;
import game.behaviours.HostileBehaviour;
import game.behaviours.WanderBehaviour;
import game.capabilities.Tameable;
import game.system.FireSystem;
import game.weapons.Claw;

import java.util.Map;
import java.util.TreeMap;

public class Bear extends Animal {
    /**
     * The constructor of the Actor class.
     *
     * @param name        the name of the Actor
     * @param displayChar the character that will represent the Actor in the
     *                    display
     * @param hitPoints   the Actor's starting hit points
     */
    public Bear(String name, char displayChar, int hitPoints) {
        super(name, displayChar, hitPoints, 50 );

        this.setIntrinsicWeapon(new Claw());
        this.behaviours.put(1, new HostileBehaviour());
        this.behaviours.put(999, new WanderBehaviour());

        this.enableAbility(Ability.CAN_ATTACK);
    }

    /**
     * Defines the logic of after being tamed
     *
     * @param actor The actor that tames this
     * @param item The item that the actor used for taming this
     * @return the description that this has been tamed by the actor
     */
    @Override
    public String tameBy(Actor actor, Item item) {
        actor.removeItemFromInventory(item);
        this.behaviours.clear();
        this.behaviours.put(1, new FightAlongSideBehaviour(actor));
        this.behaviours.put(2, new FollowBehaviour(actor));
        this.enableAbility(Ability.TAMED);
        return actor + " has tamed " + this + " using " + item;
    }
}
