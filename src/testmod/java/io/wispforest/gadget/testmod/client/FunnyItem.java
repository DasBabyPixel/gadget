package io.wispforest.gadget.testmod.client;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class FunnyItem extends Item {
    public FunnyItem() {
        super(new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("gadget-testmod", "funny"))));
    }

    @Override
    public Component getName(ItemStack stack) {
//        if (Screen.hasShiftDown()) {
//            // todo: fix this.
////            stack.getOrCreateNbt().putString("owl", "yay");
//        }

        return super.getName(stack);
    }
}
