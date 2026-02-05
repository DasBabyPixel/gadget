package io.wispforest.gadget.mixin;

import com.mojang.serialization.DataResult;
import java.util.List;
import net.minecraft.util.FileUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FileUtil.class)
public class FileUtilMixin {
    @Inject(method = "decomposePath", at = @At("HEAD"), cancellable = true)
    private static void mald(String path, CallbackInfoReturnable<DataResult<List<String>>> cir) {
        if (path.equals(""))
            cir.setReturnValue(DataResult.success(List.of()));
    }
}
