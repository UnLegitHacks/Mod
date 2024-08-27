package io.unlegit.mixins.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import io.unlegit.UnLegit;
import net.minecraft.client.renderer.ScreenEffectRenderer;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectMixin
{
    @ModifyArg(method = "renderFire", at = @At(value = "INVOKE", target = "setColor"), index = 3)
    private static float hookLowFireAlpha(float alpha)
    {
        return UnLegit.modules.get("Low Fire").isEnabled() ? alpha / 3 : alpha;
    }
}
