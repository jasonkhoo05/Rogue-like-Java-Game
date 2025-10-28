package game.positions.spawners;

import game.spawning.EveryNTurnsPolicy;

/** Cave ('C'): spawns an animal every 5 turns. */
public class Cave extends SpawnerGround {
    public Cave() {
        super('C', "Cave", new EveryNTurnsPolicy(5));
    }
}
