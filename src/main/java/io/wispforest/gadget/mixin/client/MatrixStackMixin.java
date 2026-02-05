package io.wispforest.gadget.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import io.wispforest.gadget.client.MatrixStackLogger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Deque;
import java.util.List;

@Mixin(PoseStack.class)
public class MatrixStackMixin {
    @Shadow @Final private List<PoseStack.Pose> stack;

    @Inject(method = "pop", at = @At("HEAD"), cancellable = true)
    private void onPop(CallbackInfo ci) {
        if (stack.size() == 1
         && MatrixStackLogger.tripError((PoseStack) (Object) this, "Tried to pop empty MatrixStack")) {
            ci.cancel();
            return;
        }

        MatrixStackLogger.logOp((PoseStack)(Object) this, false, stack.size() - 2);
    }

    @Inject(method = "push", at = @At("HEAD"))
    private void onPush(CallbackInfo ci) {
        MatrixStackLogger.logOp((PoseStack)(Object) this, true, stack.size() - 1);
    }
}
