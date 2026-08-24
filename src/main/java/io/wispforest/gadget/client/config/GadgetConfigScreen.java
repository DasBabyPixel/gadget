package io.wispforest.gadget.client.config;

import io.wispforest.gadget.Gadget;
import io.wispforest.owo.config.ui.ConfigScreen;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

public class GadgetConfigScreen extends ConfigScreen {
    @SuppressWarnings("unchecked")
    public GadgetConfigScreen(@Nullable Screen parent) {
        super(DEFAULT_MODEL_ID, Gadget.CONFIG, parent);
    }
}
