package game.capabilities;

import edu.monash.fit2099.engine.actors.Behaviour;

public interface BehaviourHost {
    void putBehaviour(int priority, Behaviour behaviour);
}
