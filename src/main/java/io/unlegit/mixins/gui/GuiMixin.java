package io.unlegit.mixins.gui;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import io.unlegit.UnLegit;
import io.unlegit.events.impl.render.GuiRenderE;
import io.unlegit.modules.impl.gui.ActiveMods;
import io.unlegit.modules.impl.gui.Scoreboard;
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
    
    @Inject(method = "renderScoreboardSidebar", at = @At("HEAD"), cancellable = true)
    public void hideScoreboard(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo info)
    {
        if (!UnLegit.modules.get("Scoreboard").isEnabled()) info.cancel();
    }
    
    @ModifyVariable(method = /* drawManaged */ "method_55440", at = @At("STORE"), ordinal = 4)
    public int moveScoreboardDown(int value)
    {
        Scoreboard scoreboard = (Scoreboard) UnLegit.modules.get("Scoreboard");
        ActiveMods activeMods = (ActiveMods) UnLegit.modules.get("Active Mods");
        
        int lx = (value - (minecraft.getWindow().getGuiScaledHeight() / 2)) * 3;
        return activeMods.isEnabled() && scoreboard.makeSpaceForModules.enabled ? Math.max(value, lx + activeMods.getHeight()) : value;
    }
}
