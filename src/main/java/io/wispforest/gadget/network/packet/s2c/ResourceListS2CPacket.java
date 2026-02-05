package io.wispforest.gadget.network.packet.s2c;

import java.util.Map;
import net.minecraft.resources.Identifier;

public record ResourceListS2CPacket(Map<Identifier, Integer> resources) {
}
