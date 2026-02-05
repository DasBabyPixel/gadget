package io.wispforest.gadget.mixin.client;

import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ChatComponent.class)
public interface ChatHudAccessor {
    @Invoker
    void callAddMessage(Component message, @Nullable MessageSignature signatureData, @Nullable GuiMessageTag indicator);
}
