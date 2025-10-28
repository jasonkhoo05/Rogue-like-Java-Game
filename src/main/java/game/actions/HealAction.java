package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;


public class HealAction extends Action {
    private final Actor target;
    private final int healAmount;

    public HealAction(Actor target, int healAmount) {
        this.target = target;
        this.healAmount = healAmount;
    }

    @Override
    public String execute(Actor actor, GameMap map) {
        target.heal(healAmount);
        return actor + " heals " + target + " for " + healAmount + " HP.";
    }

    @Override
    public String menuDescription(Actor actor) {
        return actor + " heals " + target + ".";
    }
}
