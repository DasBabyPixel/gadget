package io.wispforest.gadget.dump.read;

import io.wispforest.gadget.util.ContextData;
import io.wispforest.gadget.util.NetworkUtil;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;

public final class DumpedPacket extends ContextData<DumpedPacket> {
    private final boolean outbound;
    private final ConnectionProtocol phase;
    private final Packet<?> packet;
    private final ResourceLocation channelId;
    private final long sentAt;
    private final int size;

    public DumpedPacket(boolean outbound, ConnectionProtocol phase, Packet<?> packet, ResourceLocation channelId, long sentAt,
                        int size) {
        this.outbound = outbound;
        this.phase = phase;
        this.packet = packet;
        this.channelId = channelId;
        this.sentAt = sentAt;
        this.size = size;
    }

    public int color() {
        return switch (phase) {
            case PLAY -> 0xFF00FF00;
            case HANDSHAKING -> 0xFF808080;
            case LOGIN -> 0xFFFF0000;
            case STATUS -> 0xFFFFFF00;
            case CONFIGURATION -> 0xFFFFFFFF;
        };
    }

    public boolean outbound() {
        return outbound;
    }

    public ConnectionProtocol phase() {
        return phase;
    }

    public Packet<?> packet() {
        return packet;
    }

    public Object customPayload() {
        return NetworkUtil.unwrapCustom(packet);
    }

    public ResourceLocation channelId() {
        return channelId;
    }

    public long sentAt() {
        return sentAt;
    }

    public int size() {
        return size;
    }
}
