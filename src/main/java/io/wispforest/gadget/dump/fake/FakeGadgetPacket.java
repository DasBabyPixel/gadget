package io.wispforest.gadget.dump.fake;

import io.netty.buffer.ByteBuf;
import io.wispforest.gadget.dump.read.unwrapped.UnwrappedPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;
import org.jetbrains.annotations.Nullable;

/**
 * Marks a fake packet that is not meant to actually be in the network.
 */
public interface FakeGadgetPacket extends Packet<PacketListener> {
    int id();

    StreamCodec<? super FriendlyByteBuf, ? extends FakeGadgetPacket> codec();

    default Packet<?> unwrapVanilla() {
        return this;
    }

    default @Nullable UnwrappedPacket unwrapGadget() {
        throw new UnsupportedOperationException("Unrenderable packet.");
    }

    default boolean isVirtual() {
        return false;
    }

    // region vanilla stubs
    @Override
    default void handle(PacketListener listener) {
        throw new IllegalStateException();
    }

    @Override
    default PacketType<? extends Packet<PacketListener>> type() {
        throw new IllegalStateException();
    }
    // endregion
}
