package io.wispforest.gadget.mixin;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.wispforest.gadget.client.dump.ClientPacketDumper;
import net.minecraft.network.PacketEncoder;
import net.minecraft.network.ProtocolInfo;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PacketEncoder.class)
public class EncoderHandlerMixin {
    @Shadow @Final private ProtocolInfo<?> state;

    @Inject(method = "encode(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;Lio/netty/buffer/ByteBuf;)V", at = @At("HEAD"))
    private void writeHook(ChannelHandlerContext channelHandlerContext, Packet<?> packet, ByteBuf byteBuf, CallbackInfo ci) {
        if (state.flow() == PacketFlow.CLIENTBOUND) return;

        ClientPacketDumper.dump(packet, state);
    }
}
