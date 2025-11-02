package game.weather.effects;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import game.weather.WeatherEffect;
public class SunnyEffect implements WeatherEffect {
    private Display display;
    public SunnyEffect bindDisplay(Display display) {
        this.display = display;
        return this;
    }
    @Override public void onStart(GameMap map) {
        display.println("The sky clears.");
    }
    @Override public void applyPerTurn(GameMap map) { /* no-op */ }
    @Override public String name() { return "Sunny"; }
}
