package io.unlegit.mixins.entity;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import io.unlegit.UnLegit;
import io.unlegit.events.impl.entity.MotionE;
import io.unlegit.events.impl.entity.MoveE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin
{
    @Shadow @Final protected Minecraft minecraft;
    private float yaw, pitch;
    
    @Inject(method = "move", at = @At(value = "HEAD"))
    public void moveEvent(CallbackInfo info, @Local LocalRef<Vec3> vec3)
    {
        MoveE e = MoveE.get(vec3.get());
        UnLegit.events.post(e);
        vec3.set(e.vec3);
    }
    
    @Inject(method = "sendPosition", at = @At(value = "HEAD"))
    public void preMotionEvent(CallbackInfo info)
    {
        yaw = minecraft.player.getYRot(); pitch = minecraft.player.getXRot();
        Vec3 position = minecraft.player.position();
        MotionE e = MotionE.get(position.x, position.y, position.z, yaw, pitch, minecraft.player.onGround());
        UnLegit.events.post(e);
        minecraft.player.setYRot(e.yaw); minecraft.player.setXRot(e.pitch);
    }
    
    @Inject(method = "sendPosition", at = @At(value = "TAIL"))
    public void postMotionEvent(CallbackInfo info)
    {
        minecraft.player.setYRot(yaw); minecraft.player.setXRot(pitch);
    }
}
