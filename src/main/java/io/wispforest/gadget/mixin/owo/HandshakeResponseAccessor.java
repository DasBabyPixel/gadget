package io.wispforest.gadget.mixin.owo;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
import net.minecraft.resources.ResourceLocation;

@Mixin(targets = "io.wispforest.owo.network.OwoHandshake$HandshakeResponse")
public interface HandshakeResponseAccessor {
    @Accessor
    Map<ResourceLocation, Integer> getRequiredChannels();

    @Accessor
    Map<ResourceLocation, Integer> getRequiredControllers();

    @Accessor
    Map<ResourceLocation, Integer> getOptionalChannels();
}
