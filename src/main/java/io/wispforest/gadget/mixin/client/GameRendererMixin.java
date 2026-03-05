package io.wispforest.gadget.mixin.client;

import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    // TODO: idfk anymore.
//    @Inject(method = "renderLevel", at = @At("RETURN"))
//    private void checkPoseStack(DeltaTracker tickCounter, CallbackInfo ci) {
//        if (!poses.isEmpty()) {
//            MatrixStackLogger.tripError(poses, "Pose stack not empty");
//        }
//    }
}
