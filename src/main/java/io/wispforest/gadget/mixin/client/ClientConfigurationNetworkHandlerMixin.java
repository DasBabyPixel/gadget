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
public abstract class ClientConfigurationNetworkHandlerMixin extends ClientCommonPacketListenerImpl {
    protected ClientConfigurationNetworkHandlerMixin(Minecraft client, Connection connection, CommonListenerCookie connectionState) {
        super(client, connection, connectionState);
    }

    @Inject(method = "onReady", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientConnection;transitionInbound(Lnet/minecraft/network/state/NetworkState;Lnet/minecraft/network/listener/PacketListener;)V"))
    private void writeRegistries(ClientboundFinishConfigurationPacket packet, CallbackInfo ci, @Local RegistryAccess.Frozen registries) {
        if (!ClientPacketDumper.isDumping()) return;

        ClientPacketDumper.dump(GadgetDynamicRegistriesPacket.fromRegistries(registries), ConfigurationProtocols.CLIENTBOUND);
    }
}
