package game.positions;

import edu.monash.fit2099.engine.GameEngineException;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.DefaultGroundCreator;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.World;
import game.actors.*;
import game.items.weapons.Bow;
import game.positions.spawners.Cave;
import game.positions.spawners.Meadow;
import game.positions.spawners.SpawnerGround;
import game.positions.spawners.Tundra;
import game.items.weapons.Axe;
import game.items.weapons.Torch;
import game.positions.trees.AppleTree;
import game.positions.trees.HazelnutTree;
import game.positions.trees.YewBerryTree;

import game.positions.spawners.Swamp;          // new spawner type (~)
import game.spawning.newborn.BearScatterYewEffect;
import game.spawning.newborn.CrocodilePoisonAuraEffect;
import game.spawning.newborn.DeerDropAppleEffect;
import game.spawning.newborn.WolfGrowYewTreeEffect;

import game.positions.trees.stages.AppleSprout;
import game.positions.trees.stages.YewBerrySapling;


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
        //groundCreator.registerGround('T', AppleTree::new);
        groundCreator.registerGround('T', AppleSprout::new);
        groundCreator.registerGround('A', HazelnutTree::new);
        //groundCreator.registerGround('Y', YewBerryTree::new);
        groundCreator.registerGround('Y', YewBerrySapling::new);
        groundCreator.registerGround('_', Tundra::new);
        groundCreator.registerGround('C', Cave::new);
        groundCreator.registerGround('w', Meadow::new);
        groundCreator.registerGround('~', Swamp::new);  // swamp spawner


        player.addItemToInventory(new Bow());
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

        List<String> plains = Arrays.asList(
                ".........................",
                ".........................",
                ".........................",
                ".........................",
                ".........................",
                ".........................",
                ".........................",
                "........................."
        );

        GameMap plainsMap = new GameMap("Plains", groundCreator, plains);
        this.addGameMap(plainsMap);

        this.addPlayer(this.player, gameMap.at(22, 5));
        gameMap.at(24,1).addActor(new Griffin("Griffin",'G',1000));
        gameMap.at(26,4).addActor(new Unicorn("Unicorn",'U',1000));
        gameMap.at(1,8).addActor(new WiseMan("WiseMan",'☮',50));

        plainsMap.at(10, 2).setGround(new AppleSprout());
        plainsMap.at(15, 5).setGround(new YewBerrySapling());


        // -------- Cross-spawner context effects (apply to ALL spawners we create) --------
        DeerDropAppleEffect deerDropApple = new DeerDropAppleEffect(msg -> display.println(msg)); // spawn an Apple on one exit when a Deer spawns
        BearScatterYewEffect bearScatter = new BearScatterYewEffect(0.5, msg -> display.println(msg)); // each exit 50% chance to drop YewBerry when a Bear spawns
        WolfGrowYewTreeEffect wolfGrowYew = new WolfGrowYewTreeEffect(msg -> display.println(msg)); // grow special YewBerry tree on one exit when a Wolf spawns
        CrocodilePoisonAuraEffect crocPoisonNearby = new CrocodilePoisonAuraEffect(3, 10, display::print); // poison actors around spawner when a Croc spawns
        // --------------------------------------------------------------------


        // =========================
        // A2/A3 SPAWNERS WIRING
        // =========================

        // ======================= TUNDRA =======================
        // --- Forest: Tundra spawns BEARS (5%/tick, +10 HP via tundra effect)
        {
            Tundra tundraForest = new Tundra();
            tundraForest.addContextEffect(deerDropApple);
            tundraForest.addContextEffect(bearScatter);
            tundraForest.addContextEffect(wolfGrowYew);
            tundraForest.addContextEffect(crocPoisonNearby);
            tundraForest.addFactory(() -> new Bear("Bear", 'B', 200));
            gameMap.at(6, 6).setGround(tundraForest);
        }

        // --- Plains: Tundra spawns WOLVES and (A3) CROCODILES, (5% chance each tick; tundra effect gives +10 HP)
        {
            Tundra tundraPlains = new Tundra();
            tundraPlains.addContextEffect(deerDropApple);
            tundraPlains.addContextEffect(bearScatter);
            tundraPlains.addContextEffect(wolfGrowYew);
            tundraPlains.addContextEffect(crocPoisonNearby);
            tundraPlains
                    .addFactory(() -> new Wolf("Wolf", 'e', 100))
                    .addFactory(() -> new Crocodile("Crocodile", '<', 300)); // A3
            plainsMap.at(19, 3).setGround(tundraPlains);
        }

        // ======================== CAVE ========================
        // --- Forest: Cave spawns Bear/Wolf/Deer (every 5 turns)
        {
            SpawnerGround caveForest = new Cave()
                    .addContextEffect(deerDropApple)
                    .addContextEffect(bearScatter)
                    .addContextEffect(wolfGrowYew)
                    .addContextEffect(crocPoisonNearby);
            caveForest
                    .addFactory(() -> new Bear("Bear", 'B', 200))
                    .addFactory(() -> new Wolf("Wolf", 'e', 100))
                    .addFactory(() -> new Deer("Deer", 'd', 50));
            gameMap.at(17, 2).setGround(caveForest);
        }

        // --- Plains: Cave spawns Bear/Wolf (every 5 turns)
        {
            SpawnerGround cavePlains = new Cave()
                    .addContextEffect(deerDropApple)
                    .addContextEffect(bearScatter)
                    .addContextEffect(wolfGrowYew)
                    .addContextEffect(crocPoisonNearby);
            cavePlains
                    .addFactory(() -> new Bear("Bear", 'B', 200))
                    .addFactory(() -> new Wolf("Wolf", 'e', 100));
            plainsMap.at(7, 1).setGround(cavePlains);
        }


        // ======================= MEADOW =======================
        // --- Forest: Meadow spawns DEERS and (A3) CROCODILES (every 7 turns, 50%)
        // (Meadow already applies ForagingEffect to newborns)
        {
            Meadow meadowForest = new Meadow();
            meadowForest.addContextEffect(deerDropApple);
            meadowForest.addContextEffect(bearScatter);
            meadowForest.addContextEffect(wolfGrowYew);
            meadowForest.addContextEffect(crocPoisonNearby);
            meadowForest
                    .addFactory(() -> new Deer("Deer", 'd', 50))
                    .addFactory(() -> new Crocodile("Crocodile", '<', 300)); // A3 addition
            gameMap.at(30, 6).setGround(meadowForest);
        }


        // --- Plains: Meadow spawns DEERS and BEARS (unchanged)
        {
            Meadow meadowPlains = new Meadow();
            meadowPlains.addContextEffect(deerDropApple);
            meadowPlains.addContextEffect(bearScatter);
            meadowPlains.addContextEffect(wolfGrowYew);
            meadowPlains.addContextEffect(crocPoisonNearby);
            meadowPlains
                    .addFactory(() -> new Deer("Deer", 'd', 50))
                    .addFactory(() -> new Bear("Bear", 'B', 200));
            plainsMap.at(4, 4).setGround(meadowPlains);
        }

        // ======================= SWAMP =======================
        // Swamp (~): if an actor is in surrounding tiles, 50% chance to spawn.
        // Newborn animals poisoned for 10 turns @ 5 dmg/turn (handled inside Swamp via PoisonNewbornEffect).
        // --- Forest: Swamp spawns Crocodiles & Deer (50% if an actor nearby; newborn poisoned 10t x5)
        {
            Swamp swampForest = new Swamp();
            swampForest.addContextEffect(deerDropApple);
            swampForest.addContextEffect(bearScatter);
            swampForest.addContextEffect(wolfGrowYew);
            swampForest.addContextEffect(crocPoisonNearby);
            swampForest
                    .addFactory(() -> new Crocodile("Crocodile", '<', 300))
                    .addFactory(() -> new Deer("Deer", 'd', 50));
            gameMap.at(10, 3).setGround(swampForest);
        }

        // --- Plains: Swamp spawns ONLY Crocodiles (same poison-on-spawn)
        {
            Swamp swampPlains = new Swamp();
            swampPlains.addContextEffect(deerDropApple);
            swampPlains.addContextEffect(bearScatter);
            swampPlains.addContextEffect(wolfGrowYew);
            swampPlains.addContextEffect(crocPoisonNearby);
            swampPlains
                    .addFactory(() -> new Crocodile("Crocodile", '<', 300));
            plainsMap.at(12, 2).setGround(swampPlains);
        }





        // ---------- Teleporters & items (unchanged) ----------
        var doorForest = new game.positions.teleport.TeleDoor();
        var doorPlains = new game.positions.teleport.TeleDoor();

        doorForest
                .addDestination(new game.positions.teleport.TeleportDestination(gameMap, 18, 1)) // intra
                .addDestination(new game.positions.teleport.TeleportDestination(plainsMap, 3, 3)); // inter

        doorPlains
                .addDestination(new game.positions.teleport.TeleportDestination(plainsMap, 20, 6)) // intra
                .addDestination(new game.positions.teleport.TeleportDestination(gameMap, 5, 5)); // inter


        var circleForest = new game.positions.teleport.TeleportationCircle();
        var circlePlains = new game.positions.teleport.TeleportationCircle();

        circleForest
                .addDestination(new game.positions.teleport.TeleportDestination(gameMap, 10, 6)) // intra
                .addDestination(new game.positions.teleport.TeleportDestination(plainsMap, 15, 2)); // inter

        circlePlains
                .addDestination(new game.positions.teleport.TeleportDestination(plainsMap, 8, 4))   // intra
                .addDestination(new game.positions.teleport.TeleportDestination(gameMap, 12, 3)); // inter

        gameMap.at(22, 4).setGround(doorForest);
        gameMap.at(7, 5).setGround(circleForest);
        plainsMap.at(2, 6).setGround(doorPlains);
        plainsMap.at(12, 3).setGround(circlePlains);

        var cube = new game.items.TeleportCube()
                .addDestination(new game.positions.teleport.TeleportDestination(gameMap, 1, 1))
                .addDestination(new game.positions.teleport.TeleportDestination(plainsMap, 23, 6));
        player.addItemToInventory(cube);

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
                // apply stacking burn DoT to all actors once per round

                this.gameLoop();
            }
            display.println(endGameMessage());
        }
        catch (GameEngineException exception){
            display.println(exception.getMessage());
        }
    }
}
