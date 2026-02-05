package io.wispforest.gadget.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.login.ClientboundCustomQueryPacket;
import net.minecraft.network.protocol.login.ServerboundCustomQueryAnswerPacket;
import net.minecraft.resources.ResourceLocation;

public final class NetworkUtil {
    private NetworkUtil() {

    }

    public static ResourceLocation getChannelOrNull(Packet<?> packet) {
        return switch (packet) {
            case ClientboundCustomPayloadPacket pkt -> pkt.payload().type().id();
            case ServerboundCustomPayloadPacket pkt -> pkt.payload().type().id();
            case ClientboundCustomQueryPacket pkt -> pkt.payload().id();
            case null, default -> null;
        };
    }

    public static Object unwrapCustom(Packet<?> packet) {
        return switch (packet) {
            case ClientboundCustomPayloadPacket pkt -> pkt.payload();
            case ServerboundCustomPayloadPacket pkt -> pkt.payload();
            case ClientboundCustomQueryPacket pkt -> pkt.payload();
            case ServerboundCustomQueryAnswerPacket pkt when pkt.payload() != null -> pkt.payload();
            case null, default -> null;
        };
    }

    public static InfallibleClosable resetIndexes(ByteBuf buf) {
        int readerIdx = buf.readerIndex();
        int writerIdx = buf.writerIndex();

        return () -> {
            buf.readerIndex(readerIdx);
            buf.writerIndex(writerIdx);
        };
    }

    public static InfallibleClosable writeByteLength(FriendlyByteBuf buf) {
        int idIdx = buf.writerIndex();
        buf.writeInt(0);
        int startIdx = buf.writerIndex();

        return () -> {
            int endIdx = buf.writerIndex();
            buf.writerIndex(idIdx);
            buf.writeInt(endIdx - startIdx);
            buf.writerIndex(endIdx);
        };
    }
}
