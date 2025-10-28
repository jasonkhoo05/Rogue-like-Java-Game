package game.ai;


import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

/**
 * Handles generating monologues for actors using the AI API.
 */
public class Monologue {
    private final Client client;

    public Monologue() {
        // Automatically picks up GEMINI_API_KEY from environment
        this.client = new Client();
    }

    /**
     * Generates a monologue for a player based on a custom prompt.
     *
     * @param prompt Description of the player's state or situation
     * @return AI-generated monologue as a String
     */
    public String generate(String prompt) {
        GenerateContentResponse response = client.models.generateContent(
                "gemini-2.5-flash",
                prompt,
                null
        );
        return response.text();
    }
}
