package io.unlegit.mixins.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.unlegit.UnLegit;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

@Mixin(targets = "net.minecraft.client.renderer.FogRenderer$BlindnessFogFunction")
public class FogRenderMixin
{
    @Inject(method = "getMobEffect", at = @At(value = "HEAD"), cancellable = true)
    public void modifyMobEffectBlindness(CallbackInfoReturnable<Holder<MobEffect>> effect)
    {
        if (UnLegit.modules.get("Anti Blind").isEnabled()) effect.setReturnValue(MobEffects.UNLUCK);
    }
}
