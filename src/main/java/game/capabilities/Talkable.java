package game.capabilities;

public interface Talkable {
    /**
     * Generate a monologue based on context.
     * @param context The prompt for AI generation.
     * @return AI-generated monologue.
     */
    String performMonologue(String context);
}
