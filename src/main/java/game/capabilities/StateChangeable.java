package game.capabilities;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;

public interface StateChangeable {
    void changeState(Actor actor, Display display);

}
