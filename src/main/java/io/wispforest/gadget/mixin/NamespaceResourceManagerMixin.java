package io.wispforest.gadget.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.wispforest.gadget.Gadget;
import io.wispforest.gadget.pond.MixinState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Collection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.FallbackResourceManager;

@Mixin(FallbackResourceManager.class)
public class NamespaceResourceManagerMixin {
    @WrapOperation(method = "findAndAdd", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourcePack;findResources(Lnet/minecraft/resource/ResourceType;Ljava/lang/String;Ljava/lang/String;Lnet/minecraft/resource/ResourcePack$ResultConsumer;)V"))
    private void ignoreErrorsIfNeeded(PackResources pack, PackType type, String namespace, String prefix, PackResources.ResourceOutput consumer, Operation<Collection<ResourceLocation>> original) {
        if (MixinState.IS_IGNORING_ERRORS.get() != null) {
            try {
                original.call(pack, type, namespace, prefix, consumer);
            } catch (Exception e) {
                Gadget.LOGGER.error("Resource pack {} threw an error while loading all resources, which has been ignored", pack.packId());
            }
        } else {
            original.call(pack, type, namespace, prefix, consumer);
        }
    }
}
