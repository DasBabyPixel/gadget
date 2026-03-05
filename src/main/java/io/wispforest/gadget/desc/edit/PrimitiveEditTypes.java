package io.wispforest.gadget.desc.edit;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.wispforest.endec.Endec;
import io.wispforest.gadget.util.ReflectionUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class PrimitiveEditTypes {
    private static final BiMap<String, PrimitiveEditType<?>> REGISTRY = HashBiMap.create();
    private static final Map<Class<?>, PrimitiveEditType<?>> CLASS_TO_TYPE = new HashMap<>();

    static final Endec<PrimitiveEditType<?>> ENDEC = Endec.STRING.xmap(REGISTRY::get, REGISTRY.inverse()::get);

    private PrimitiveEditTypes() {

    }

    public static <T> void register(String name, Class<T> klass, PrimitiveEditType<T> type) {
        REGISTRY.put(name, type);
        CLASS_TO_TYPE.put(klass, type);
    }

    public static <T> void registerSimple(String name, Class<T> klass, Function<String, T> fromImpl, Function<T, String> toImpl) {
        register(name, klass, new SimpleEditType<>(fromImpl, toImpl));
    }

    public static <T> void registerForRegistry(Class<T> klass, Registry<T> registry) {
        register(registry.key().identifier().toString(), klass, new RegistryEditType<>(registry));
    }

    @SuppressWarnings("unchecked")
    public static <T> PrimitiveEditType<T> getFor(Class<T> klass) {
        return (PrimitiveEditType<T>) ReflectionUtil.findFor(klass, CLASS_TO_TYPE);
    }

    @SuppressWarnings("unchecked")
    public static void init() {
        registerSimple("boolean", Boolean.class, Boolean::parseBoolean, Object::toString);
        registerSimple("int", Integer.class, Integer::parseInt, Object::toString);
        registerSimple("long", Long.class, Long::parseLong, Object::toString);
        registerSimple("float", Float.class, Float::parseFloat, Object::toString);
        registerSimple("double", Double.class, Double::parseDouble, Object::toString);
        registerSimple("string", String.class, x -> x, String::toString);
        registerSimple("identifier", Identifier.class, Identifier::parse, Identifier::toString);
        registerSimple("uuid", UUID.class, UUID::fromString, UUID::toString);

        registerForRegistry(Block.class, BuiltInRegistries.BLOCK);
        registerForRegistry(Item.class, BuiltInRegistries.ITEM);
        registerForRegistry((Class<EntityType<?>>)(Class<?>) EntityType.class, BuiltInRegistries.ENTITY_TYPE);
        registerForRegistry((Class<BlockEntityType<?>>)(Class<?>) BlockEntityType.class, BuiltInRegistries.BLOCK_ENTITY_TYPE);
        registerForRegistry(MobEffect.class, BuiltInRegistries.MOB_EFFECT);
    }
}
