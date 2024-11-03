package io.unlegit.mixins.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import io.unlegit.UnLegit;
import net.minecraft.client.renderer.LightTexture;

@Mixin(LightTexture.class)
public class LightTextureMixin
{
    @Redirect(method = "updateLightTexture", at = @At(value = "INVOKE", target = "Ljava/lang/Double;floatValue()F", ordinal = 1))
    private float fullBright(Double value)
    {
        return UnLegit.modules.get("Full Bright").isEnabled() ? 100 : value.floatValue();
    }
}
