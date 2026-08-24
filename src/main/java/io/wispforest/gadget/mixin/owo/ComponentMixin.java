package io.wispforest.gadget.mixin.owo;

import io.wispforest.gadget.Gadget;
import io.wispforest.owo.ui.core.ParentUIComponent;
import io.wispforest.owo.ui.core.UIComponent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = UIComponent.class)
public interface ComponentMixin {
    @Shadow @Nullable ParentUIComponent parent();

    @Inject(method = "remove", at = @At("HEAD"), cancellable = true)
    private void remove(CallbackInfo ci) {
        if (!Gadget.CONFIG.errorCheckOwoUi()) return;

        ci.cancel();

        ParentUIComponent currentParent = parent();

        if (currentParent == null) return;

        Throwable context = new Throwable("UIComponent#remove was called here");

        currentParent.queue(() -> {
            ParentUIComponent newParent = parent();

            if (newParent == currentParent) {
                currentParent.removeChild((UIComponent) this);
            } else {
                throw new IllegalStateException("UIComponent " + this + "'s parent changed from " + currentParent + " to " + newParent + " after UIComponent$remove() call", context);
            }
        });
    }
}
