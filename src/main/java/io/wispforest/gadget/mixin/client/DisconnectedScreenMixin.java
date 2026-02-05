package io.wispforest.gadget.mixin.client;

import io.wispforest.gadget.client.dump.ClientPacketDumper;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.DisconnectionDetails;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DisconnectedScreen.class)
public class DisconnectedScreenMixin {
    @Inject(method = "<init>(Lnet/minecraft/client/gui/screen/Screen;Lnet/minecraft/text/Text;Lnet/minecraft/network/DisconnectionInfo;Lnet/minecraft/text/Text;)V", at = @At("TAIL"))
    private void disableDump(Screen parent, Component title, DisconnectionDetails info, Component buttonLabel, CallbackInfo ci) {
        if (ClientPacketDumper.isDumping())
            ClientPacketDumper.stop();
    }
}
