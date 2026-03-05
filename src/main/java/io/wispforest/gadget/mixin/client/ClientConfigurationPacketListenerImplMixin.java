package io.wispforest.gadget.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import io.wispforest.gadget.client.dump.ClientPacketDumper;
import io.wispforest.gadget.dump.fake.GadgetDynamicRegistriesPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientConfigurationPacketListenerImpl;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.configuration.ClientboundFinishConfigurationPacket;
import net.minecraft.network.protocol.configuration.ConfigurationProtocols;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConfigurationPacketListenerImpl.class)
public abstract class ClientConfigurationPacketListenerImplMixin extends ClientCommonPacketListenerImpl {
    protected ClientConfigurationPacketListenerImplMixin(Minecraft minecraft, Connection connection, CommonListenerCookie connectionState) {
        super(minecraft, connection, connectionState);
    }

    @Inject(method = "handleConfigurationFinished", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Connection;setupInboundProtocol(Lnet/minecraft/network/ProtocolInfo;Lnet/minecraft/network/PacketListener;)V"))
    private void writeRegistries(ClientboundFinishConfigurationPacket packet, CallbackInfo ci, @Local RegistryAccess.Frozen registries) {
        if (!ClientPacketDumper.isDumping()) return;

        ClientPacketDumper.dump(GadgetDynamicRegistriesPacket.fromRegistries(registries), ConfigurationProtocols.CLIENTBOUND);
    }
}
