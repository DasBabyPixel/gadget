package io.wispforest.gadget.client.gui;

import io.wispforest.owo.ui.container.WrappingParentUIComponent;
import io.wispforest.owo.ui.core.OwoUIGraphics;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.core.UIComponent;
import net.minecraft.client.input.MouseButtonEvent;

/**
 * Eats events. Yum!
 * <p> Mainly used to make {@link io.wispforest.owo.ui.container.OverlayContainer} think that the click was inside the box
 * and not close.
 *
 * @param <C> Wrapped {@link UIComponent} type.
 */
public class EventEaterWrapper<C extends UIComponent> extends WrappingParentUIComponent<C> {
    public EventEaterWrapper(C child) {
        super(Sizing.content(), Sizing.content(), child);
    }

    @Override
    public boolean onMouseDown(MouseButtonEvent click, boolean doubled) {
        return super.onMouseDown(click, doubled) || isInBoundingBox(click.x(), click.y());
    }

    @Override
    public boolean onMouseUp(MouseButtonEvent click) {
        return super.onMouseUp(click) || isInBoundingBox(click.x(), click.y());
    }

    @Override
    public void draw(OwoUIGraphics ctx, int mouseX, int mouseY, float partialTicks, float delta) {
        super.draw(ctx, mouseX, mouseY, partialTicks, delta);

        this.drawChildren(ctx, mouseX, mouseY, partialTicks, delta, children());
    }
}
