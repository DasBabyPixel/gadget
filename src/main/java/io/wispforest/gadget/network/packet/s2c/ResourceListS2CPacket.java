package io.wispforest.gadget.network.packet.s2c;

import java.util.Map;
import net.minecraft.resources.ResourceLocation;

public record ResourceListS2CPacket(Map<ResourceLocation, Integer> resources) {
}
