package game.weather;
import edu.monash.fit2099.engine.positions.GameMap;

public interface WeatherEffect {
    /** Called once when weather starts. */
    default void onStart(GameMap map) {}

    /** Called every turn while active. */
    void applyPerTurn(GameMap map);

    /** Called once when weather ends. */
    default void onEnd(GameMap map) {}

    /** For HUD/logging. */
    String name();
}
