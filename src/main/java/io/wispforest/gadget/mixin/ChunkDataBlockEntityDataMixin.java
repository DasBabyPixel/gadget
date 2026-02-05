package io.wispforest.gadget.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net/minecraft/network/packet/s2c/play/ChunkData$BlockEntityData")
public class ChunkDataBlockEntityDataMixin {
    @Unique private int gadget$originalBlockEntityTypeId = -1;

    @Inject(method = "<init>(Lnet/minecraft/network/RegistryByteBuf;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/codec/PacketCodec;decode(Ljava/lang/Object;)Ljava/lang/Object;"))
    private void saveId(RegistryFriendlyByteBuf buf, CallbackInfo ci) {
        int readerIdx = buf.readerIndex();

        try {
            gadget$originalBlockEntityTypeId = buf.readVarInt();
        } finally {
            buf.readerIndex(readerIdx);
        }
    }

    @WrapWithCondition(method = "write", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/codec/PacketCodec;encode(Ljava/lang/Object;Ljava/lang/Object;)V"))
    private boolean useId(StreamCodec<?, ?> instance, Object buf, Object type) {
        if (type == null) {
            ((FriendlyByteBuf) buf).writeVarInt(gadget$originalBlockEntityTypeId);
            return false;
        } else {
            return true;
        }
    }

}
