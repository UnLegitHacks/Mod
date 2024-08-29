package io.unlegit.mixins.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import io.unlegit.UnLegit;
import io.unlegit.events.impl.render.GuiRenderE;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;

@Mixin(Gui.class)
public class GuiMixin
{
    @Inject(method = "render", at = @At(value = "INVOKE", target = "disableDepthTest", shift = Shift.AFTER))
    public void renderEvent(CallbackInfo info, @Local LocalRef<GuiGraphics> graphics, @Local LocalRef<DeltaTracker> deltaTracker)
    {
        UnLegit.events.post(GuiRenderE.get(graphics.get(), deltaTracker.get().getGameTimeDeltaTicks()));
    }
}
