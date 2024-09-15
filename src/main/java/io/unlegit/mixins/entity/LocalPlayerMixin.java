package io.unlegit.mixins.entity;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import io.unlegit.UnLegit;
import io.unlegit.events.impl.entity.MotionE;
import io.unlegit.events.impl.entity.MoveE;
import io.unlegit.utils.entity.RotationUtil.GCDFix;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin
{
    @Shadow @Final protected Minecraft minecraft;
    @Shadow public Input input;
    private boolean sprinting;
    private float yaw, pitch;
    
    @Inject(method = "move", at = @At(value = "HEAD"))
    public void moveEvent(CallbackInfo info, @Local LocalRef<Vec3> vec3)
    {
        MoveE e = MoveE.get(vec3.get());
        UnLegit.events.post(e);
        vec3.set(e.vec3);
    }
    
    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "isUsingItem"))
    public void noSlowHook(CallbackInfo info)
    {
        if (UnLegit.modules.get("No Slow").isEnabled() && isUsingItem())
        {
            input.leftImpulse /= 0.2F;
            input.forwardImpulse /= 0.2F;
        }
    }
    
    @Inject(method = "sendPosition", at = @At(value = "HEAD"))
    public void preMotionEvent(CallbackInfo info)
    {
        yaw = minecraft.player.getYRot(); pitch = minecraft.player.getXRot();
        sprinting = minecraft.player.isSprinting();
        Vec3 position = minecraft.player.position();
        MotionE e = MotionE.get(position.x, position.y, position.z, yaw, pitch, minecraft.player.onGround(), sprinting);
        UnLegit.events.post(e);
        
        // Automatically applies GCD Fix for us
        if (e.yaw != yaw || e.pitch != pitch)
        {
            float[] bypassRotations = GCDFix.get(new float[] {e.yaw, e.pitch});
            e.yaw = bypassRotations[0]; e.pitch = bypassRotations[1];
        }
        
        minecraft.player.setYRot(e.yaw); minecraft.player.setXRot(e.pitch);
        minecraft.player.setSprinting(e.sprinting);
    }
    
    @Inject(method = "sendPosition", at = @At(value = "TAIL"))
    public void postMotionEvent(CallbackInfo info)
    {
        minecraft.player.setYRot(yaw); minecraft.player.setXRot(pitch);
        minecraft.player.setSprinting(sprinting);
    }
    
    @Shadow public boolean isUsingItem() { return false; }
}
