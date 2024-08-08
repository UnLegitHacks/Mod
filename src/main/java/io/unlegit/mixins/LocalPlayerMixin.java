package io.unlegit.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import io.unlegit.UnLegit;
import io.unlegit.events.impl.MoveE;
import io.unlegit.events.impl.UpdateE;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin
{
    @Inject(method = "tick", at = @At(value = "INVOKE", target = "tick"))
    public void updateEvent(CallbackInfo info)
    {
        UnLegit.events.post(UpdateE.get());
    }
    
    @Inject(method = "move", at = @At(value = "HEAD"))
    public void moveEvent(CallbackInfo info, @Local LocalRef<Vec3> vec3)
    {
        MoveE e = MoveE.get(vec3.get());
        UnLegit.events.post(e);
        vec3.set(e.vec3);
    }
}
