package io.wispforest.gadget.mixin;

import net.minecraft.core.Registry;
import net.minecraft.tags.TagNetworkSerialization;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TagNetworkSerialization.class)
public interface TagPacketSerializerAccessor {
    @Invoker("serializeTags")
    static <T> TagNetworkSerialization.NetworkPayload serializeTags(Registry<T> registry) {
        throw new IllegalStateException();
    }
}
