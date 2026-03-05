package io.wispforest.gadget.dump.read.unwrapped;

import io.wispforest.gadget.util.ErrorSink;
import io.wispforest.gadget.util.FormattedDumper;
import io.wispforest.owo.ui.component.UIComponents;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Insets;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;

/**
 * A helper {@link UnwrappedPacket} rendered as colorful Text.
 */
public interface LinesUnwrappedPacket extends UnwrappedPacket {
    void render(Consumer<Component> out, ErrorSink errSink);

    @Override
    default void dumpAsPlainText(FormattedDumper out, int indent, ErrorSink errSink) {
        render(line -> out.write(indent, line.getString()), errSink);
    }

    @Environment(EnvType.CLIENT)
    @Override
    default void render(FlowLayout out, ErrorSink errSink) {
        render(line ->
            out.child(UIComponents.label(line)
                .margins(Insets.bottom(3))),
            errSink);
    }
}
