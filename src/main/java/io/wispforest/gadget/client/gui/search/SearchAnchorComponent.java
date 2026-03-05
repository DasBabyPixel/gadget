// Taken from https://github.com/wisp-forest/owo-lib/blob/1.19/src/main/java/io/wispforest/owo/config/ui/component/SearchAnchorComponent.java.
// Includes modifications.

package io.wispforest.gadget.client.gui.search;

import io.wispforest.owo.ui.base.BaseUIComponent;
import io.wispforest.owo.ui.core.OwoUIGraphics;
import io.wispforest.owo.ui.core.ParentUIComponent;
import io.wispforest.owo.ui.core.Positioning;
import io.wispforest.owo.ui.core.Sizing;
import java.util.StringJoiner;
import java.util.function.Supplier;

public class SearchAnchorComponent extends BaseUIComponent {

    protected final ParentUIComponent anchorFrame;
    protected final Supplier<String>[] searchTextSources;


    @SafeVarargs
    public SearchAnchorComponent(ParentUIComponent anchorFrame, Supplier<String>... searchTextSources) {
        this.anchorFrame = anchorFrame;
        this.searchTextSources = searchTextSources;

        this.positioning(Positioning.absolute(0, 0));
        this.sizing(Sizing.fixed(0));
    }

    @Override
    public void draw(OwoUIGraphics ctx, int mouseX, int mouseY, float partialTicks, float delta) {}

    public ParentUIComponent anchorFrame() {
        return this.anchorFrame;
    }

    public String currentSearchText() {
        StringJoiner sj = new StringJoiner(" ");
        for (var supplier : searchTextSources) {
            sj.add(supplier.get());
        }
        return sj.toString();
    }
}
