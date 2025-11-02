package game.weather.effects;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperation;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.attributes.AnimalAttribute;
import game.actors.attributes.PlayerAttribute;
import game.status.HeatEmpowered;
import game.weather.WeatherEffect;

public class HeatwaveEffect implements WeatherEffect {
    private static final int WARMTH_CAP = 99;
    private static final int INCREASE_AMOUNT = 10;
    private Display display;
    public HeatwaveEffect bindDisplay(Display display) {
        this.display = display;
        return this;
    }
    @Override public void onStart(GameMap map) {
        if (display != null) display.println("Heatwave rises. Everything scorches.");
    }

    @Override
    public void applyPerTurn(GameMap map) {
        for (int y : map.getYRange()) {
            for (int x : map.getXRange()) {
                Location loc = map.at(x, y);
                if (!loc.containsAnActor()) continue;

                Actor a = loc.getActor();
                a.addStatus(new HeatEmpowered(2));

                boolean updated = false;

                // try the player's WARMTH
                try {
                    int curr = a.getAttribute(PlayerAttribute.WARMTH);
                    int newVal = Math.min(WARMTH_CAP, curr + INCREASE_AMOUNT);
                    a.modifyAttribute(PlayerAttribute.WARMTH, ActorAttributeOperation.UPDATE, newVal);
                    updated = true;
                } catch (Exception ignored) {}

                // Try animal WARMTH
                if (!updated) {
                    try {
                        int curr = a.getAttribute(AnimalAttribute.WARMTH);
                        int newVal = Math.min(WARMTH_CAP, curr + INCREASE_AMOUNT);
                        a.modifyAttribute(AnimalAttribute.WARMTH, ActorAttributeOperation.UPDATE, newVal);
                        updated = true;
                    } catch (Exception ignored) {}
                }
            }
        }
    }

    @Override public void onEnd(GameMap map) {
        if (display != null) display.println("Heatwave fades.");
    }
    @Override public String name() { return "Heatwave"; }
}