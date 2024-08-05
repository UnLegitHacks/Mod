package io.unlegit.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.unlegit.UnLegit;
import io.unlegit.events.impl.GuiRenderE;
import net.minecraft.client.gui.Gui;

@Mixin(Gui.class)
public class GuiMixin
{
    @Inject(method = "render", at = @At(value = "INVOKE", target = "render", shift = Shift.AFTER))
    public void renderEvent(CallbackInfo info)
    {
        UnLegit.eventBus.post(GuiRenderE.EVENT);
    }
}
