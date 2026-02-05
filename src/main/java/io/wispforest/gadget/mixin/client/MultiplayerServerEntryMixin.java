package io.wispforest.gadget.mixin.client;

import io.wispforest.gadget.Gadget;
import io.wispforest.gadget.client.dump.ClientPacketDumper;
import io.wispforest.gadget.client.dump.DumpPrimer;
import io.wispforest.gadget.client.gui.ContextMenuScreens;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.UnknownHostException;

@Mixin(ServerSelectionList.OnlineServerEntry.class)
public abstract class MultiplayerServerEntryMixin {
    @Shadow @Final private JoinMultiplayerScreen screen;

    @Shadow @Final private ServerData server;

    @Shadow @Final private Minecraft client;

    @Shadow public abstract void saveFile();

    @Shadow protected abstract void update();

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void onRightClick(MouseButtonEvent click, boolean doubled, CallbackInfoReturnable<Boolean> cir) {
        if (click.button() != GLFW.GLFW_MOUSE_BUTTON_RIGHT) return;
        if (!Gadget.CONFIG.rightClickDump()) return;

        ContextMenuScreens.contextMenuAt(screen, click.x(), click.y())
            .button(Component.translatable("text.gadget.join_with_dump"), dropdown2 -> {
                DumpPrimer.isPrimed = true;

                this.screen.join(server);
            })
            .button(Component.translatable("text.gadget.query_with_dump"), dropdown2 -> {
                ClientPacketDumper.start(false);

                try {
                    this.screen.getPinger().pingServer(
                        this.server,
                        () -> this.client.execute(this::saveFile),
                        () -> {
                            this.server
                                .setState(
                                    this.server.protocol == SharedConstants.getCurrentVersion().protocolVersion()
                                        ? ServerData.State.SUCCESSFUL
                                        : ServerData.State.INCOMPATIBLE
                                );
                            this.client.execute(this::update);
                        }
                    );
                } catch (UnknownHostException var2x) {
                    this.server.ping = -1L;
                    this.server.motd = MultiplayerServerListWidgetAccessor.getCANNOT_RESOLVE_TEXT();
                } catch (Exception var3x) {
                    this.server.ping = -1L;
                    this.server.motd = MultiplayerServerListWidgetAccessor.getCANNOT_CONNECT_TEXT();
                }
            });

        cir.setReturnValue(true);
    }
}