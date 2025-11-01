package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import game.Ability;
import game.capabilities.HasRecipeJournal;
import game.items.RecipeJournal;

class FakeActor extends Actor implements HasRecipeJournal {
    private final boolean isPlayer;
    private final RecipeJournal journal = new RecipeJournal();

    FakeActor(boolean isPlayer) {
        super("FakeActor", 'A', 100);
        this.isPlayer = isPlayer;
        if (isPlayer) {
            this.enableAbility(Ability.IS_PLAYER);
        }

    }

    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        return new DoNothingAction();
    }

    @Override
    public RecipeJournal getRecipeJournal() {
        return journal;
    }
}

