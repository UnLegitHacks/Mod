package io.unlegit.mixins.render;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import io.unlegit.UnLegit;
import io.unlegit.events.impl.render.GuiRenderE;
import io.unlegit.modules.impl.gui.ActiveMods;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;

@Mixin(Gui.class)
public class GuiMixin
{
    @Shadow @Final private Minecraft minecraft;
    
    @Inject(method = "render", at = @At(value = "INVOKE", target = "disableDepthTest", shift = Shift.BEFORE))
    public void renderEvent(CallbackInfo info, @Local LocalRef<GuiGraphics> graphics, @Local LocalRef<DeltaTracker> deltaTracker)
    {
        UnLegit.events.post(GuiRenderE.get(graphics.get(), deltaTracker.get().getGameTimeDeltaTicks()));
    }
    
    @ModifyVariable(method = /* drawManaged */ "method_55440", at = @At("STORE"), ordinal = 4)
    public int moveScoreboardDown(int value)
    {
        ActiveMods activeMods = (ActiveMods) UnLegit.modules.get("Active Mods");
        return activeMods.isEnabled() ? Math.max(value, (int) (value / 2) + activeMods.getHeight() + 15) : value;
    }
}
