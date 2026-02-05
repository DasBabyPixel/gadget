package io.wispforest.gadget.client.gui.inspector;

import io.wispforest.gadget.mixin.client.EntryListWidgetAccessor;
import io.wispforest.gadget.mixin.client.EntryListWidgetEntryAccessor;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.AbstractWidget;

public final class VanillaSupport {
    private VanillaSupport() {

    }

    public static void init() {
        ElementUtils.registerRootLister((screen, list) -> list.add(screen));

        ElementUtils.registerElementSupport(AbstractWidget.class, ElementSupport.fromLambda(
            AbstractWidget::getX,
            AbstractWidget::getY,
            AbstractWidget::getWidth,
            AbstractWidget::getHeight
        ));

        ElementUtils.registerElementSupport(EntryListWidgetAccessor.class, ElementSupport.fromLambda(
            w -> ((AbstractWidget) w).getX(),
            w -> ((AbstractWidget) w).getY(),
            w -> ((AbstractWidget) w).getWidth(),
            w -> ((AbstractWidget) w).getHeight()
        ));

        ElementUtils.registerElementSupport(AbstractSelectionList.Entry.class, ElementSupport.fromLambda(
            w -> {
                var list = ((EntryListWidgetEntryAccessor) w).getParentList();
                return list.getRowLeft();
            },
            w -> {
                var list = ((EntryListWidgetEntryAccessor) w).getParentList();
                return ((EntryListWidgetAccessor) list).callGetRowTop(list.children().indexOf(w));
            },
            w -> {
                var list = ((EntryListWidgetEntryAccessor) w).getParentList();
                return list.getRowWidth();
            },
            w -> {
                var list = ((EntryListWidgetEntryAccessor) w).getParentList();
                return ((EntryListWidgetAccessor) list).getItemHeight();
            }
        ));
    }
}
