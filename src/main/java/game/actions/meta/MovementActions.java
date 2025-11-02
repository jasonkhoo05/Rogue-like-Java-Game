package game.actions.meta;

import edu.monash.fit2099.engine.actions.Action;

import java.util.Set;

/**
 * Minimal, engine-agnostic movement action detector.
 * Uses class names (strings) to avoid instanceof / engine changes.
 */
public final class MovementActions {
    private MovementActions() {}

    // All Action class names that "cause displacement/transfer/teleportation" will be inside here.
    private static final Set<String> MOVEMENT_CLASS_NAMES = Set.of(
            // standard engine movement
            "edu.monash.fit2099.engine.actions.MoveActorAction",
            "game.actions.TeleportAction"
            //...
    );

    public static boolean isMovement(Action a) {
        return MOVEMENT_CLASS_NAMES.contains(a.getClass().getName());
    }
}
