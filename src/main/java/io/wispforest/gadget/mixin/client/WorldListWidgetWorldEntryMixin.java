package io.wispforest.gadget.mixin.client;

import io.wispforest.gadget.Gadget;
import io.wispforest.gadget.client.dump.DumpPrimer;
import io.wispforest.gadget.client.gui.ContextMenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldSelectionList.WorldListEntry.class)
public abstract class WorldListWidgetWorldEntryMixin {
    @Shadow @Final private Screen screen;

    @Shadow public abstract void play();

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void onRightClick(MouseButtonEvent click, boolean doubled, CallbackInfoReturnable<Boolean> cir) {
        if (click.button() != GLFW.GLFW_MOUSE_BUTTON_RIGHT) return;
        if (!Gadget.CONFIG.rightClickDump()) return;

        ContextMenuScreens.contextMenuAt(screen, click.x(), click.y())
                .button(Component.translatable("text.gadget.join_with_dump"), dropdown2 -> {
                    DumpPrimer.isPrimed = true;
                    play();
                });

        cir.setReturnValue(true);
    }
}
