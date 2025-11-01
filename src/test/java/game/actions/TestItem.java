package game.actions;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import game.capabilities.Consumable;

public class TestItem extends Item implements Consumable {

    private final String consumeMessage;

    public TestItem(String name, String consumeMessage) {
        super(name, 'T', true); // display char doesn't matter
        this.consumeMessage = consumeMessage;
    }

    @Override
    public String consumedBy(Actor actor) {
        return consumeMessage;
    }
}
