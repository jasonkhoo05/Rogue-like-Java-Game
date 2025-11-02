package game.capabilities;
/**
 * The {@code Talkable} interface should be implemented by any actor
 * that can perform a monologue or speech in the game.
 *
 * Implementing classes define how the monologue is generated or displayed.
 */
public interface Talkable {


    /**
     * Performs a monologue based on the given context.
     *
     * @param context a description of the situation or state influencing the monologue
     * @return a {@link String} containing the generated or predefined monologue
     */
    String performMonologue(String context);
}
