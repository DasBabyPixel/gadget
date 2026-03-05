package io.wispforest.gadget.mixin.owo;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "io.wispforest.owo.particles.systems.ParticleSystemController$ParticleSystemPayload")
public interface ParticleSystemPayloadAccessor extends CustomPacketPayload {
    @Accessor
    Vec3 getPos();
}
