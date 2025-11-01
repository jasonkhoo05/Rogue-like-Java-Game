package game.weather.effects;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.weather.WeatherEffect;

import java.util.List;
import java.util.Random;

public class TornadoEffect implements WeatherEffect {
    private Display display;
    public TornadoEffect bindDisplay(Display display) {
        this.display = display;
        return this;
    }
    private final Random rng = new Random();

    @Override public void onStart(GameMap map) {
        display.println("Tornadoes spawn across the land.");
    }

    @Override
    public void applyPerTurn(GameMap map) {
        // collect all the actor positions that may be blown away this round.
        java.util.ArrayList<Location> actorLocs = new java.util.ArrayList<>();
        for (int y : map.getYRange()) {
            for (int x : map.getXRange()) {
                Location loc = map.at(x, y);
                if (loc.containsAnActor()) {
                    actorLocs.add(loc);
                }
            }
        }
        if (actorLocs.isEmpty()) return;

        // handles a maximum of K times to avoid massive operations
        final int MAX_EVENTS = Math.min(10, Math.max(3, actorLocs.size() / 8));
        final double CHANCE = 0.15;
        final int DAMAGE = 10;

        // To maintain randomness, shuffle once
        java.util.Collections.shuffle(actorLocs, rng);

        int used = 0;
        StringBuilder sb = new StringBuilder();
        for (Location here : actorLocs) {
            if (used >= MAX_EVENTS) break;
            if (!here.containsAnActor()) continue;
            if (rng.nextDouble() >= CHANCE) continue;

            Actor a = here.getActor();
            // Randomly select an accessible neighboring cell
            List<Exit> exits = here.getExits();
            if (exits.isEmpty()) continue;

            // Try several times to find a destination you can enter, avoiding a vicious cycle
            boolean moved = false;
            for (int tries = 0; tries < Math.min(4, exits.size()); tries++) {
                Location dest = exits.get(rng.nextInt(exits.size())).getDestination();
                if (!dest.containsAnActor() && dest.canActorEnter(a)) {
                    map.moveActor(a, dest);
                    a.hurt(DAMAGE);
                    moved = true;
                    break;
                }
            }

            if (moved) {
                used++;
                if (display != null && used <= 6) { // Print only the first few prints
                    sb.append(a).append(" is swept by a tornado!\n");
                }
            }
        }

        // Standardize printing
        if (display != null && sb.length() > 0) {
            display.println(sb.toString().trim());
        }
    }

    @Override public void onEnd(GameMap map) {
        display.println("The air calms.");
    }
    @Override public String name() { return "Tornado"; }
}
