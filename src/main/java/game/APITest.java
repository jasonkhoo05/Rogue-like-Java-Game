package game;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

public class APITest {
    public static void main(String[] args) {
        // The client gets the API key from the environment variable `GEMINI_API_KEY`.
//        Client client = Client.builder().apiKey("AIzaSyA9wfjgcfSlaF5YcS9D__LpD00NXc2tHug").build();
        Client client = new Client();

        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-2.5-flash",
                        "Write a short monologue of hows the character day is going",
                        null);

        System.out.println(response.text());
    }
}
