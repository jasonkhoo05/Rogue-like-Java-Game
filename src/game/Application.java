package game;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Item;
import game.actors.Player;
import game.items.Bedroll;
import game.items.Bottle;
import game.positions.Earth;

import java.util.ArrayList;

public class Application {
    /**
     * Runs the main game
     * @param args the arguments
     */
    public static void main(String[] args) {
        Display terminalDisplay = new Display();

        ArrayList<Item> items = new ArrayList<>();
        items.add(new Bedroll("Bedroll", '=', true));
        items.add(new Bottle("Bottle", 'o', true));

        Player player = new Player("Explorer", 'ඞ', 100, 20, 30, items);
        Earth earth = new Earth(terminalDisplay, player);
        try{
            for (String line : FancyMessage.GAME_TITLE.split("\n")) {
                new Display().println(line);
                try {
                    Thread.sleep(200);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

            earth.constructWorld();
            earth.run();
        }
        catch (Exception e) {
            // General exception, to help debugging.
            e.printStackTrace();
        }
    }
}
