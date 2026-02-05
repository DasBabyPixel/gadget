package io.wispforest.gadget.client.gui;

import io.wispforest.owo.ui.container.WrappingParentUIComponent;
import io.wispforest.owo.ui.core.OwoUIGraphics;
import io.wispforest.owo.ui.core.Size;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.core.UIComponent;

public class LayoutCacheWrapper<C extends UIComponent> extends WrappingParentUIComponent<C> {
    private Size prevSpace;

    public LayoutCacheWrapper(C child) {
        super(Sizing.content(), Sizing.content(), child);
    }

    @Override
    public void layout(Size space) {
        if (!space.equals(prevSpace)) {
            prevSpace = space;
            this.child.inflate(this.calculateChildSpace(space));
        }

        this.child.mount(this, this.childMountX(), this.childMountY());
    }

    @Override
    public void onChildMutated(UIComponent child) {
        this.prevSpace = null;
        super.onChildMutated(child);
    }

    @Override
    public void draw(OwoUIGraphics ctx, int mouseX, int mouseY, float partialTicks, float delta) {
        super.draw(ctx, mouseX, mouseY, partialTicks, delta);

        this.drawChildren(ctx, mouseX, mouseY, partialTicks, delta, children());
    }
}
