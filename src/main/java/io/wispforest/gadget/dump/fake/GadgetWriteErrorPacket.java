package io.wispforest.gadget.dump.fake;

import io.netty.buffer.ByteBuf;
import io.wispforest.gadget.dump.read.unwrapped.UnwrappedPacket;
import io.wispforest.gadget.util.ThrowableUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record GadgetWriteErrorPacket(int packetId, String exceptionText) implements FakeGadgetPacket {
    public static final int ID = -1;
    public static final StreamCodec<ByteBuf, GadgetWriteErrorPacket> CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT, GadgetWriteErrorPacket::packetId,
        ByteBufCodecs.STRING_UTF8, GadgetWriteErrorPacket::exceptionText,
        GadgetWriteErrorPacket::new
    );

    public static GadgetWriteErrorPacket fromThrowable(int packetId, Throwable t) {
        return new GadgetWriteErrorPacket(packetId, ThrowableUtil.throwableToString(t));
    }

    @Override
    public int id() {
        return ID;
    }

    @Override
    public StreamCodec<ByteBuf, GadgetWriteErrorPacket> codec() {
        return CODEC;
    }

    @Override
    public UnwrappedPacket unwrapGadget() {
        // Don't render anything.
        return UnwrappedPacket.NULL;
    }
}
