package game.spawning;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperation;
import edu.monash.fit2099.engine.actors.attributes.BaseAttributes;

public class HealthBonusEffect implements SpawnEffect {
    private final int bonus;
    public HealthBonusEffect(int bonus) { this.bonus = bonus; }

    @Override
    public void apply(Actor newborn) {
        // 1) increase the maximum health pool
        newborn.modifyStatsMaximum(BaseAttributes.HEALTH, ActorAttributeOperation.INCREASE, bonus);
        // 2) then increase current HP to actually give them the bonus now
        newborn.modifyAttribute(BaseAttributes.HEALTH, ActorAttributeOperation.INCREASE, bonus);
    }
}
