// game/weather/WeatherSystem.java
package game.weather;

import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

public class WeatherSystem {
    private final Map<WeatherType, WeatherEffect> effects = new EnumMap<>(WeatherType.class);
    private final Random rng = new Random();

    private WeatherType current = WeatherType.SUNNY;
    private int turnsLeft = 3;

    // announcer
    private Display announcer;
    private WeatherType lastAnnounced = null;

    public WeatherSystem register(WeatherType type, WeatherEffect effect) {
        effects.put(type, effect);
        return this;
    }

    // Let the weather system handle the display.
    public WeatherSystem setAnnouncer(Display display) {
        this.announcer = display;
        return this;
    }

    // Getter for printing on Earth
    public WeatherType getCurrentWeather() { return current; }
    public int getTurnsLeft() { return turnsLeft; }

    public void tick(GameMap map) {
        if (turnsLeft == nextDuration()) {
            WeatherEffect startEffect = effects.get(current);
            if (startEffect != null) startEffect.onStart(map);
            if (announcer != null)
                announcer.println("[Weather] " + current + " (" + turnsLeft + " turns)");
        }

        WeatherEffect effect = effects.get(current);
        if (effect != null) {
            effect.applyPerTurn(map);
        }

        turnsLeft--;

        if (turnsLeft > 0) return;

        WeatherEffect endEffect = effects.get(current);
        if (endEffect != null) endEffect.onEnd(map);

        WeatherType before = current;
        current = pickNextWeather(before);
        turnsLeft = nextDuration();
        announceChange(before, current, turnsLeft);
    }


    private void announceChange(WeatherType from, WeatherType to, int duration) {
        if (announcer == null) return;
        if (lastAnnounced == to) return;

        announcer.println("[Weather] " + to + " (" + duration + " turns)");

        lastAnnounced = to;
    }

    private WeatherType pickNextWeather(WeatherType exclude) {
        WeatherType[] types = effects.keySet().toArray(new WeatherType[0]);
        if (types.length == 0) return WeatherType.SUNNY;
        WeatherType next = types[rng.nextInt(types.length)];
        // Avoid repeating the same as last time
        if (types.length > 1 && next == exclude) {
            next = types[(rng.nextInt(types.length - 1) + 1) % types.length];
        }
        return next;
    }

    private int nextDuration() {
        return 3;
    }
}
