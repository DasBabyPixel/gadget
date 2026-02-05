package io.wispforest.gadget.util;

import io.wispforest.gadget.pond.MixinState;
import java.util.List;
import java.util.Map;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

public class ResourceUtil {
    private ResourceUtil() {

    }

    public static Map<Identifier, List<Resource>> collectAllResources(ResourceManager manager) {
        try {
            MixinState.IS_IGNORING_ERRORS.set(true);
            return manager.listResourceStacks("", x -> true);
        } finally {
            MixinState.IS_IGNORING_ERRORS.remove();
        }
    }
}
