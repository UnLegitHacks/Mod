package io.unlegit.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.unlegit.gui.UnLegitOptions;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@Mixin(PauseScreen.class)
public class PauseScreenMixin extends Screen
{
    protected PauseScreenMixin(Component component) { super(component); }
    
    @Inject(method = "createPauseMenu", at = @At(value = "INVOKE", target = "arrangeElements", shift = At.Shift.BEFORE))
    public void addUnLegitOptionsButton(CallbackInfo info)
    {
        addRenderableWidget(Button.builder(Component.literal("UnLegit Options"),
                button -> minecraft.setScreen(UnLegitOptions.screen()))
                .bounds((width / 2) - 100, height - 26, 200, 20).build());
    }
}