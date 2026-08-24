package io.wispforest.gadget.decompile.handle;


import io.wispforest.gadget.Gadget;
import io.wispforest.gadget.decompile.KnotUtil;
import io.wispforest.gadget.decompile.OpenedURLClassLoader;
import io.wispforest.gadget.util.ProgressToast;
import net.minecraft.network.chat.Component;
import org.jetbrains.java.decompiler.main.Fernflower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class QuiltflowerHandlerImpl implements io.wispforest.gadget.decompile.QuiltflowerHandler {
    private final Map<String, byte[]> unmappedClassBytecodeStash = new HashMap<>();
    private final Map<String, byte[]> classBytecodeStash = new HashMap<>();
    private final List<String> allUnmappedClasses;
    final Consumer<Component> logConsumer;

    public QuiltflowerHandlerImpl(ProgressToast toast, Consumer<Component> logConsumer) {
        this.logConsumer = logConsumer;

        allUnmappedClasses = new ArrayList<>();

        for (Class<?> klass : KnotUtil.INSTRUMENTATION.getInitiatedClasses(Gadget.class.getClassLoader())) {
            if (klass.isHidden()) continue;
            if (klass.isArray()) continue;

            allUnmappedClasses.add(klass.getName().replace('.', '/'));
        }
    }

    @Override
    public byte[] getClassBytes(String path) {
        if (path.endsWith(".class"))
            path = path.substring(0, path.length() - 6);

        return unmappedClassBytecodeStash.computeIfAbsent(path.replace('/', '.'),
            name2 -> KnotUtil.getPostMixinClassByteArray(name2, true));
    }

    public List<String> allUnmappedClasses() {
        return allUnmappedClasses;
    }

    @Override
    public String decompileClass(Class<?> klass) {
        GadgetResultSaver resultSaver = new GadgetResultSaver();
        Fernflower fernflower = new Fernflower(resultSaver, Map.of("ind", "    "), new GadgetFernflowerLogger(this));

        fernflower.addSource(new ClassContextSource(this, klass));

        if (Gadget.CONFIG.fullDecompilationContext())
            fernflower.addLibrary(new EverythingContextSource(this));

        fernflower.decompileContext();
        fernflower.clearContext();
        return resultSaver.saved;
    }

    static {
        var cl = (OpenedURLClassLoader) QuiltflowerHandlerImpl.class.getClassLoader();

        if (cl == Gadget.class.getClassLoader())
            throw new UnsupportedOperationException("Quiltflower handler was loaded on Knot!");
    }
}
