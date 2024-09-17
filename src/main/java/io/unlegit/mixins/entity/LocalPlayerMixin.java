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
    private boolean sprinting, onGround;
    @Shadow public Input input;
    private float yaw, pitch;
    private double x, y, z;
    
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
        Vec3 position = minecraft.player.position();
        x = position.x; y = position.y; z = position.z;
        yaw = minecraft.player.getYRot(); pitch = minecraft.player.getXRot();
        onGround = minecraft.player.onGround();
        sprinting = minecraft.player.isSprinting();
        MotionE e = MotionE.get(x, y, z, yaw, pitch, onGround, sprinting);
        UnLegit.events.post(e);
        
        // Automatically applies GCD Fix for us
        if (e.changed)
        {
            float[] bypassRotations = GCDFix.get(new float[] {e.yaw, e.pitch});
            e.yaw = bypassRotations[0]; e.pitch = bypassRotations[1];
        }
        
        minecraft.player.setYRot(e.yaw); minecraft.player.setXRot(e.pitch);
        minecraft.player.setSprinting(e.sprinting);
        minecraft.player.setOnGround(e.onGround);
        minecraft.player.setPosRaw(e.x, e.y, e.z);
    }
    
    @Inject(method = "sendPosition", at = @At(value = "TAIL"))
    public void postMotionEvent(CallbackInfo info)
    {
        minecraft.player.setYRot(yaw); minecraft.player.setXRot(pitch);
        minecraft.player.setSprinting(sprinting);
        minecraft.player.setOnGround(onGround);
        minecraft.player.setPosRaw(x, y, z);
    }
    
    @Shadow public boolean isUsingItem() { return false; }
}
