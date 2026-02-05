package io.wispforest.gadget.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.platform.InputConstants;
import io.wispforest.gadget.Gadget;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyboardHandler.class)
public abstract class KeyboardMixin {
    @Shadow @Final private Minecraft client;

    @Shadow protected abstract boolean processF3(KeyEvent input);

    @ModifyExpressionValue(method = "onKey", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;keyPressed(Lnet/minecraft/client/input/KeyInput;)Z"))
    private boolean afterKeyPressed(boolean original, long window, int action, KeyEvent input) {
        var client = Minecraft.getInstance();

        if (original) return true;
        if (!Gadget.CONFIG.debugKeysInScreens()) return false;
        if (!InputConstants.isKeyDown(client.getWindow(), GLFW.GLFW_KEY_F3)) return false;

        return ((KeyboardMixin)(Object) client.keyboardHandler).processF3(input);
    }

    @Inject(method = "processF3", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;player:Lnet/minecraft/client/network/ClientPlayerEntity;"), cancellable = true)
    private void leaveIfPlayer(KeyEvent keyInput, CallbackInfoReturnable<Boolean> cir) {
        if (client.player == null)
            cir.setReturnValue(false);
    }

    @Inject(method = "processF3", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;hasPermissionLevel(I)Z"), cancellable = true)
    private void leaveOnGameModeSelection(KeyEvent keyInput, CallbackInfoReturnable<Boolean> cir) {
        if (client.player == null)
            cir.setReturnValue(false);
    }
}
