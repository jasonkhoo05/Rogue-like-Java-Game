package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.ai.Monologue;
import game.capabilities.HasRecipeJournal;
import game.capabilities.Talkable;
import game.items.RecipeJournal;

import java.util.concurrent.ThreadLocalRandom;

/**
 * An action that allows an actor to read their Recipe Journal.
 * <p>
 * When executed, the journal content is displayed. If at least one recipe exists,
 * the actor will also generate a short AI-based comment about one of the recipes.
 * This uses the {@link Monologue} generator to produce a short opinion similar to
 * an explorer’s remark.
 */
public class ReadJournalAction extends Action {
    private final Monologue monologueAI = new Monologue();

    /**
     * Reads the journal and optionally generates a comment on a random recipe.
     *
     * @param actor the actor performing the action
     * @param map   the current game map
     * @return a string containing the journal content and an optional AI comment
     */
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

    /**
     * Generates a short comment about a given recipe using {@link TalkAction}.
     *
     * @param actor  the actor making the comment
     * @param map    the game map
     * @param recipe the recipe to comment on
     * @return a string containing the comment text
     */
    protected String executeTalkAction(Actor actor, GameMap map, String recipe) {
        Talkable talkable = (prompt) -> {
            String aiPrompt = "Write one short 20-word opinion an explorer would say about the food: " + prompt;
            return monologueAI.generate(aiPrompt);
        };

        TalkAction commentAction = new TalkAction(talkable, recipe);
        return commentAction.execute(actor, map);
    }

    /**
     * Describes how the action will appear in the action menu.
     *
     * @param actor the actor viewing the menu
     * @return a short description for the menu
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " reads recipe Journal";
    }
}
