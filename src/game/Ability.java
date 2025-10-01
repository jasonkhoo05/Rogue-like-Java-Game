package game;

import edu.monash.fit2099.engine.capabilities.Status;

/**
 * Use this enum to represent abilities.
 * Example #1: if the player can jump over walls, you can attach Abilities.WALL_JUMP to the Player class
 */
public enum Ability
{
    CAN_ATTACK,
    ITEMSPAWNABLE,
    CAN_TAME,
    TAMED,
    HEALABLE,
    RECEIVED_HEAL,
    IMMUNITY,
    BOOST_DAMAGE,
    IS_PLAYER,
}
