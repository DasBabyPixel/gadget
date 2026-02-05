package io.wispforest.gadget.util;

import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Supplier;

public final class NetworkUtil {
    private NetworkUtil() {

    }

    public static ResourceLocation getChannelOrNull(Packet<?> packet) {
        return switch (packet) {
            case CustomPayloadS2CPacket pkt -> pkt.payload().getId().id();
            case CustomPayloadC2SPacket pkt -> pkt.payload().getId().id();
            case LoginQueryRequestS2CPacket pkt -> pkt.payload().id();
            case null, default -> null;
        };
    }

    public static Object unwrapCustom(Packet<?> packet) {
        return switch (packet) {
            case CustomPayloadS2CPacket pkt -> pkt.payload();
            case CustomPayloadC2SPacket pkt -> pkt.payload();
            case LoginQueryRequestS2CPacket pkt -> pkt.payload();
            case LoginQueryResponseC2SPacket pkt when pkt.response() != null -> pkt.response();
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
