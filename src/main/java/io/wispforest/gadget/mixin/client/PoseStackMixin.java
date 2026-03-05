package io.wispforest.gadget.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import io.wispforest.gadget.client.PoseStackLogger;
import java.util.List;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PoseStack.class)
public class PoseStackMixin {
    @Shadow @Final private List<PoseStack.Pose> poses;

    @Inject(method = "popPose", at = @At("HEAD"), cancellable = true)
    private void onPop(CallbackInfo ci) {
        if (poses.size() == 1
         && PoseStackLogger.tripError((PoseStack) (Object) this, "Tried to pop empty PoseStack")) {
            ci.cancel();
            return;
        }

        PoseStackLogger.logOp((PoseStack)(Object) this, false, poses.size() - 2);
    }

    @Inject(method = "pushPose", at = @At("HEAD"))
    private void onPush(CallbackInfo ci) {
        PoseStackLogger.logOp((PoseStack)(Object) this, true, poses.size() - 1);
    }
}
