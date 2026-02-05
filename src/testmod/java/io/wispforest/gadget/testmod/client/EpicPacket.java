package io.wispforest.gadget.testmod.client;

import io.wispforest.endec.Endec;
import io.wispforest.endec.impl.StructEndecBuilder;
import io.wispforest.gadget.Gadget;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record EpicPacket(String maldenhagen) implements CustomPacketPayload {
    public static final Type<EpicPacket> TYPE = new Type<>(Gadget.id("epic"));
    public static final Endec<EpicPacket> ENDEC = StructEndecBuilder.of(
        Endec.STRING.fieldOf("maldenhagen", EpicPacket::maldenhagen),
        EpicPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
