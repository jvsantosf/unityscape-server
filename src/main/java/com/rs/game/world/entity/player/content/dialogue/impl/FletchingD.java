package com.rs.game.world.entity.player.content.dialogue.impl;

import com.rs.game.item.Item;
import com.rs.game.world.entity.player.actions.skilling.Fletching;
import com.rs.game.world.entity.player.actions.skilling.Fletching.Fletch;
import com.rs.game.world.entity.player.content.SkillsDialogue;
import com.rs.game.world.entity.player.content.SkillsDialogue.ItemNameFilter;
import com.rs.game.world.entity.player.content.dialogue.Dialogue;

public class FletchingD extends Dialogue {

    private Fletch items;

    // componentId, amount, option

    @Override
    public void start() {
        items = (Fletch) parameters[0];
        boolean maxQuantityTen = Fletching.maxMakeQuantityTen(new Item(items.getSelected()));
        SkillsDialogue
                .sendSkillsDialogue(
                        player,
                        maxQuantityTen ? SkillsDialogue.MAKE_NO_ALL_NO_CUSTOM
                                : SkillsDialogue.MAKE,
                        "Choose how many you wish to make,<br>then click on the item to begin.",
                        maxQuantityTen ? 10 : 28, items.getProduct(),
                        maxQuantityTen ? null : new ItemNameFilter() {
                            @Override
                            public String rename(String name) {
                                return name.replace(" (u)", "");
                            }
                        });
    }

    @Override
    public void run(int interfaceId, int componentId) {
        int option = SkillsDialogue.getItemSlot(componentId);
        if (option > items.getProduct().length) {
            end();
            return;
        }
        int quantity = SkillsDialogue.getQuantity(player);
        int invQuantity = player.getInventory().getItems()
                .getNumberOf(items.getId());
        if (quantity > invQuantity)
            quantity = invQuantity;
        player.getActionManager().setAction(
                new Fletching(items, option, quantity));
        end();
    }

    @Override
    public void finish() {
    }

}