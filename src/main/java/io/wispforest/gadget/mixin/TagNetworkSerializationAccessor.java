package io.wispforest.gadget.mixin;

import net.minecraft.core.Registry;
import net.minecraft.tags.TagNetworkSerialization;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TagNetworkSerialization.class)
public interface TagNetworkSerializationAccessor {
    @Invoker("serializeToNetwork")
    static <T> TagNetworkSerialization.NetworkPayload serializeToNetwork(Registry<T> registry) {
        throw new IllegalStateException();
    }
}
