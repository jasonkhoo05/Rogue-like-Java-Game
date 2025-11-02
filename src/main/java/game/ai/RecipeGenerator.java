package game.ai;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

/**
 * The {@code RecipeGenerator} class is responsible for generating survival recipes
 * using the AI API based on a given item.
 */
public class RecipeGenerator {
    private final Client client;

    /**
     * Constructs a {@code RecipeGenerator} using the default AI client.
     * The client picks up the GEMINI_API_KEY from the environment.
     */
    public RecipeGenerator() {
        this(new Client()); // uses GEMINI_API_KEY from env
}


    /**
     * Constructs a {@code RecipeGenerator} with a custom AI client.
     * Primarily used for testing or dependency injection.
     *
     * @param client the AI API client to use
     */
    // Test-friendly constructor
    public RecipeGenerator(Client client) {
        this.client = client;
    }


    /**
     * Generates a short survival recipe using the given item name.
     *
     * @param itemName the name of the item to generate a recipe for
     * @return a {@link String} containing the AI-generated recipe
     */
    public String generateRecipe(String itemName) {
        String prompt = "Generate one short real survival recipe(only include name, ingredients and short step to cook) using " + itemName + ".";
        GenerateContentResponse response = client.models.generateContent("gemini-2.5-flash", prompt, null);
        return response.text();
    }
}

