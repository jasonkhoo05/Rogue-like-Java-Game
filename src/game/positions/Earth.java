package game.positions;

import edu.monash.fit2099.engine.GameEngineException;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.World;
import game.actors.Bear;
import game.actors.Deer;
import game.actors.Player;
import game.actors.Wolf;
import game.items.weapons.Axe;
import game.items.weapons.Torch;
import game.positions.trees.AppleTree;
import game.positions.trees.HazelnutTree;
import game.positions.trees.YewBerryTree;

import java.util.Arrays;
import java.util.List;

public class Earth extends World {
    private final Player player;

    /**
     * Constructor
     * @param display the Display that will display this World.
     * @param player the player in this world
     */
    public Earth(Display display, Player player) {
        super(display);

        this.player = player;
    }

    /**
     * Constructs the world
     * @throws Exception the exception thrown if any error occurs
     */
    public void constructWorld() throws Exception {
        DefaultGroundCreator groundCreator = new DefaultGroundCreator();
        groundCreator.registerGround('.', Snow::new);
        groundCreator.registerGround('T', AppleTree::new);
        groundCreator.registerGround('A', HazelnutTree::new);
        groundCreator.registerGround('Y', YewBerryTree::new);
        player.addItemToInventory(new Torch());
        player.addItemToInventory(new Axe());
        List<String> map = Arrays.asList(
                "........................................",
                "........................................",
                "........................................",
                "........................................",
                "........................................",
                "........................................",
                ".............A.........T................",
                "....................Y...................",
                "........................................",
                "........................................"
        );

        GameMap gameMap = new GameMap("Forest", groundCreator, map);
        this.addGameMap(gameMap);

        this.addPlayer(this.player, gameMap.at(22, 5));
        gameMap.at(25,6).addActor(new Deer("Deer", 'd', 50));
        gameMap.at(0,0).addActor(new Wolf("Wolf", 'e', 100));
        gameMap.at(3,3).addActor(new Bear("Bear", 'B', 200));
    }

    /**
     * Runs the game
     */
    @Override
    public void run() {
        try{
            if (player == null)
                throw new IllegalStateException();

            // initialize the last action map to nothing actions;
            for (Actor actor : actorLocations) {
                lastActionMap.put(actor, new DoNothingAction());
            }

            // This loop is basically the whole game
            while (stillRunning() && this.player.isConscious()) {
                this.gameLoop();
            }
            display.println(endGameMessage());
        }
        catch (GameEngineException exception){
            display.println(exception.getMessage());
        }
    }
}
