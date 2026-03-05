package io.wispforest.gadget.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.wispforest.gadget.client.dump.ClientPacketDumper;
import java.util.List;
import net.minecraft.network.PacketDecoder;
import net.minecraft.network.ProtocolInfo;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PacketDecoder.class)
public class PacketDecoderMixin {
    @Shadow @Final private ProtocolInfo<?> protocolInfo;

    @Inject(method = "decode", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ProtocolSwapHandler;handleInboundTerminalPacket(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/protocol/Packet;)V"))
    private void readHook(ChannelHandlerContext context, ByteBuf buf, List<Object> objects, CallbackInfo ci, @Local Packet<?> packet) {
        if (protocolInfo.flow() == PacketFlow.SERVERBOUND) return;

        ClientPacketDumper.dump(packet, protocolInfo);
    }
}
