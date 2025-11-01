package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.ai.Monologue;
import game.capabilities.HasRecipeJournal;
import game.capabilities.Talkable;
import game.items.RecipeJournal;

import java.util.concurrent.ThreadLocalRandom;


public class ReadJournalAction extends Action {
    private final Monologue monologueAI = new Monologue();

    @Override
    public String execute(Actor actor, GameMap map) {
        // Safely get the player's recipe journal
        var maybeJournal = actor.asCapability(HasRecipeJournal.class);
        if (maybeJournal.isEmpty()) {
            return actor + " has no recipe journal.";
        }

        RecipeJournal recipeJournal = maybeJournal.get().getRecipeJournal();
        String journalContent = recipeJournal.toString();

        String comment = "";

        // Only comment if there are recipes
        if (!recipeJournal.getRecipes().isEmpty()) {
            // Pick a random recipe
            int randomIndex = ThreadLocalRandom.current().nextInt(recipeJournal.getRecipes().size());
            String randomRecipe = recipeJournal.getRecipes().get(randomIndex);

            // Talkable uses AI to generate a monologue based on the recipe
            Talkable talkable = (prompt) -> {
                String aiPrompt = "Write one short 20-word opinion an explorer would say about the food: " + prompt;
                return monologueAI.generate(aiPrompt);
            };

            // Use TalkAction to produce the AI opinion
            TalkAction commentAction = new TalkAction(talkable, randomRecipe);
            comment = "\n" +  executeTalkAction(actor, map, randomRecipe);
        }

        return journalContent + comment;
    }

    protected String executeTalkAction(Actor actor, GameMap map, String recipe) {
        Talkable talkable = (prompt) -> {
            String aiPrompt = "Write one short 20-word opinion an explorer would say about the food: " + prompt;
            return monologueAI.generate(aiPrompt);
        };

        TalkAction commentAction = new TalkAction(talkable, recipe);
        return commentAction.execute(actor, map);
    }


    @Override
    public String menuDescription(Actor actor) {
        return actor + " reads recipe Journal";
    }
}
