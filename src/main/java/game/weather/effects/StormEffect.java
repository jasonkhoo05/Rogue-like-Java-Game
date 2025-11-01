package game.weather.effects;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.positions.DamagedGround;
import game.status.MovementLocked;
import game.weather.WeatherEffect;

import java.util.Random;

public class StormEffect implements WeatherEffect {
    private final Random rng = new Random();
    private Display display;

    private static final int STRIKES_PER_TURN = 3;
    private static final int ACTOR_DAMAGE = 12;
    private static final int LOCK_TURNS   = 3;

    public StormEffect bindDisplay(Display display) {
        this.display = display;
        return this;
    }

    @Override
    public void onStart(GameMap map) {
        if (display != null) display.println("A thunderstorm rolls in.");
    }

    @Override
    public void applyPerTurn(GameMap map) {
        final int width  = map.getXRange().max() + 1;
        final int height = map.getYRange().max() + 1;

        StringBuilder sb = new StringBuilder();
        int printed = 0;
        final int PRINT_LIMIT = 6;

        for (int i = 0; i < STRIKES_PER_TURN; i++) {
            int x = rng.nextInt(width);
            int y = rng.nextInt(height);
            Location loc = map.at(x, y);

            if (loc.containsAnActor()) {
                // when hit actor
                Actor a = loc.getActor();
                a.hurt(ACTOR_DAMAGE);
                a.addStatus(new MovementLocked(LOCK_TURNS));
                if (display != null && printed < PRINT_LIMIT) {
                    sb.append(a).append(" is struck by lightning and cannot move for ")
                            .append(LOCK_TURNS).append(" turns!\n");
                    printed++;
                }
            } else {
                // hit ground
                loc.setGround(new DamagedGround());
                if (display != null && printed < PRINT_LIMIT) {
                    sb.append("Lightning scorches the ground at (")
                            .append(x).append(", ").append(y).append(")!\n");
                    printed++;
                }
            }
        }

        if (display != null && sb.length() > 0) {
            display.println(sb.toString().trim());
        }
    }

    @Override
    public void onEnd(GameMap map) {
        if (display != null) display.println("The storm passes.");
    }

    @Override
    public String name() { return "Storm"; }
}
