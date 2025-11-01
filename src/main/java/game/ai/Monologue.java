package game.ai;


import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;


/**
 * The {@code Monologue} class is responsible for generating AI-powered monologues for game actors.
 * It serves as a wrapper around the AI API client, handling the request and returning the generated text.
 * <p>
 * This class supports dependency injection to allow for easier testing and mocking of the AI client.
 * </p>
 */
public class Monologue {
    private final Client client;

    /**
     * Constructs a {@code Monologue} instance using the default AI client.
     * <p>
     * The default constructor automatically picks up the API key from the environment.
     * </p>
     */
    public Monologue() {
        this(new Client()); // uses environment API key
    }

    /**
     * Constructs a {@code Monologue} instance using a custom {@link Client}.
     * <p>
     * This constructor is primarily intended for testing purposes, allowing
     * a mock or alternative client to be injected.
     * </p>
     *
     * @param client the AI API client to use for generating monologues
     */
    // Test-friendly constructor for dependency injection
    public Monologue(Client client) {
        this.client = client;
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
