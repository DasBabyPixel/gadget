 package io.wispforest.gadget.dump.read.handler;

import io.wispforest.gadget.dump.read.unwrapped.FieldsUnwrappedPacket;
import io.wispforest.gadget.dump.read.unwrapped.LinesUnwrappedPacket;
import io.wispforest.gadget.mixin.owo.*;
import io.wispforest.gadget.util.ErrorSink;
import io.wispforest.gadget.util.NetworkUtil;
import io.wispforest.owo.network.OwoHandshake;
import io.wispforest.owo.particles.systems.ParticleSystemController;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.OptionalInt;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("UnstableApiUsage")
public final class OwoSupport {
    private OwoSupport() {

    }

    public static void init() {
        PacketUnwrapper.EVENT.register((packet, errSink) -> {
            if (!(packet.customPayload() instanceof MessagePayloadAccessor payload)) return null;

            return new ChannelPacket(payload.getMessage());
        });

        Method instanceGetter;
        try {
            var klass = Class.forName("io.wispforest.owo.particles.systems.ParticleSystemController$ParticleSystemPayload");
            instanceGetter = klass.getMethod("instance");
            instanceGetter.setAccessible(true);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        PacketUnwrapper.EVENT.register((packet, errSink) -> {
            if (!(packet.customPayload() instanceof ParticleSystemPayloadAccessor payload)) return null;

            ParticleSystemController controller = ParticleSystemController.REGISTERED_CONTROLLERS.get(payload.type().id());
            Vec3 pos = payload.getPos();
            ParticleSystemInstanceAccessor<?> instance;

            try {
                instance = (ParticleSystemInstanceAccessor<?>) instanceGetter.invoke(payload);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }

            int index = ((ParticleSystemAccessor) instance.getSystem()).getIndex();

            return new ParticleSystemPacket(controller, index, pos, instance.getData());
        });

        PacketUnwrapper.EVENT.register((packet, errSink) -> {
            if (!(packet.customPayload() instanceof OwoHandshake.HandshakeRequest payload)) return null;

            return new HandshakeRequest(payload);
        });

        PacketUnwrapper.EVENT.register((packet, errSink) -> {
            if (!(packet.customPayload() instanceof HandshakeResponseAccessor payload)) return null;

            return new HandshakeResponse(payload.getRequiredChannels(), payload.getRequiredControllers(), payload.getOptionalChannels());
        });
    }

    private static void drawHandshakeMap(Map<ResourceLocation, Integer> data, Component prefix, Consumer<Component> out) {
        for (var entry : data.entrySet()) {
            out.accept(Component.literal("")
                .append(prefix)
                .append(Component.literal(entry.getKey().toString())
                    .withStyle(ChatFormatting.WHITE))
                .append(Component.literal(" = " + entry.getValue())
                    .withStyle(ChatFormatting.GRAY)));
        }
    }

    public record ParticleSystemPacket(ParticleSystemController controller, int systemId, Vec3 pos, Object data) implements FieldsUnwrappedPacket {
        @Override
        public Component headText() {
            return Component.translatable("text.gadget.particle_system", systemId, (int) pos.x, (int) pos.y, (int) pos.z);
        }

        @Override
        public @Nullable Object rawFieldsObject() {
            return data;
        }

        @Override
        public OptionalInt packetId() {
            return OptionalInt.of(systemId);
        }
    }

    public record ChannelPacket(Object packetData) implements FieldsUnwrappedPacket {
        @Override
        public @Nullable Object rawFieldsObject() {
            return packetData;
        }
    }

    public record HandshakeRequest(OwoHandshake.HandshakeRequest req) implements LinesUnwrappedPacket {
        @Override
        public void render(Consumer<Component> out, ErrorSink errSink) {
            drawHandshakeMap(req.optionalChannels(), Component.literal("o ").withStyle(ChatFormatting.AQUA), out);
        }
    }

    public record HandshakeResponse(Map<ResourceLocation, Integer> requiredChannels,
                                    Map<ResourceLocation, Integer> requiredControllers,
                                    Map<ResourceLocation, Integer> optionalChannels) implements LinesUnwrappedPacket {
        @Override
        public void render(Consumer<Component> out, ErrorSink errSink) {
            drawHandshakeMap(requiredChannels, Component.literal("r ").withStyle(ChatFormatting.RED), out);
            drawHandshakeMap(requiredControllers, Component.literal("p ").withStyle(ChatFormatting.GREEN), out);
            drawHandshakeMap(optionalChannels, Component.literal("o ").withStyle(ChatFormatting.AQUA), out);
        }
    }
}
