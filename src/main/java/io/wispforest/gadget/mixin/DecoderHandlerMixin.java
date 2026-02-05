package io.wispforest.gadget.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.wispforest.gadget.client.dump.ClientPacketDumper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import net.minecraft.network.PacketDecoder;
import net.minecraft.network.ProtocolInfo;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;

@Mixin(PacketDecoder.class)
public class DecoderHandlerMixin {
    @Shadow @Final private ProtocolInfo<?> state;

    @Inject(method = "decode", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/handler/NetworkStateTransitionHandler;onDecoded(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V"))
    private void readHook(ChannelHandlerContext context, ByteBuf buf, List<Object> objects, CallbackInfo ci, @Local Packet<?> packet) {
        if (state.flow() == PacketFlow.SERVERBOUND) return;

        ClientPacketDumper.dump(packet, state);
    }
}
