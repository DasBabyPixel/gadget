package io.wispforest.gadget.dump.write;

import io.netty.buffer.ByteBuf;
import io.wispforest.gadget.Gadget;
import io.wispforest.gadget.dump.fake.FakeGadgetPacket;
import io.wispforest.gadget.dump.fake.GadgetDynamicRegistriesPacket;
import io.wispforest.gadget.dump.fake.GadgetReadErrorPacket;
import io.wispforest.gadget.dump.fake.GadgetWriteErrorPacket;
import io.wispforest.gadget.util.SlicingPacketByteBuf;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.ProtocolInfo;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;

public final class PacketDumping {
    private static final Int2ObjectMap<StreamCodec<? super FriendlyByteBuf, ? extends FakeGadgetPacket>> PACKETS = new Int2ObjectOpenHashMap<>();

    private PacketDumping() {

    }

    static {
        register(GadgetWriteErrorPacket.ID, GadgetWriteErrorPacket.CODEC);
//        register(GadgetReadErrorPacket.ID, GadgetReadErrorPacket.CODEC);
        register(GadgetDynamicRegistriesPacket.ID, GadgetDynamicRegistriesPacket.CODEC);

    }

    public static void register(int id, StreamCodec<? super FriendlyByteBuf, ? extends FakeGadgetPacket> codec) {
        if (PACKETS.put(id, codec) != null) {
            throw new IllegalStateException("Codec on " + id + " collides with another codec");
        }
    }

    @SuppressWarnings("unchecked")
    public static void writePacket(FriendlyByteBuf buf, Packet<?> packet, ProtocolInfo<?> state) {
        int startWriteIdx = buf.writerIndex();
        int packetId = 0;

        try {
            if (packet instanceof FakeGadgetPacket fakePacket) {
                packetId = fakePacket.id();
                buf.writeVarInt(packetId);
                ((StreamCodec<ByteBuf, FakeGadgetPacket>) fakePacket.codec()).encode(buf, fakePacket);
                return;
            }

            ((StreamCodec<ByteBuf, Object>)(Object) state.codec()).encode(new SlicingPacketByteBuf(buf), packet);
        } catch (Exception e) {
            buf.writerIndex(startWriteIdx);

            Gadget.LOGGER.error("Error while writing packet {}", packet, e);

            GadgetWriteErrorPacket writeError = GadgetWriteErrorPacket.fromThrowable(packetId, e);
            buf.writeVarInt(writeError.id());
            writeError.codec().encode(buf, writeError);
        }
    }

    public static Packet<?> readPacket(FriendlyByteBuf buf, ProtocolInfo<?> state) {
        int startOfData = buf.readerIndex();
        int packetId = buf.readVarInt();

        try {
            StreamCodec<? super FriendlyByteBuf, ? extends FakeGadgetPacket> fakeCodec = PACKETS.get(packetId);
            if (fakeCodec != null) {
                return fakeCodec.decode(buf).unwrapVanilla();
            }

            buf.readerIndex(startOfData);

            return state.codec().decode(buf);
        } catch (Exception e) {
            buf.readerIndex(startOfData);
            return GadgetReadErrorPacket.from(buf, packetId, e);
        }
    }
}
