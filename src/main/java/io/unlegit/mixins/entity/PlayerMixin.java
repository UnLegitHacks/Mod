package io.unlegit.mixins.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;

import io.unlegit.UnLegit;
import io.unlegit.events.impl.entity.MotionE;
import io.unlegit.modules.ModuleU;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

@Mixin(Player.class)
public class PlayerMixin
{
    private Minecraft minecraft = Minecraft.getInstance();
    
    @Redirect(method = "attack", at = @At(value = "INVOKE", target = "setSprinting"))
    public void keepSprint(Player player, boolean bl)
    {
        ModuleU keepSprint = UnLegit.modules.get("Keep Sprint");
        if (!keepSprint.isEnabled() || !player.is(minecraft.player)) player.setSprinting(bl);
    }
    
    @WrapWithCondition(method = "attack", at = @At(value = "INVOKE", target = "setDeltaMovement", ordinal = 0))
    private boolean slowVelocity(Player player, Vec3 vec3)
    {
        ModuleU keepSprint = UnLegit.modules.get("Keep Sprint");
        return !(player.is(minecraft.player) && keepSprint.isEnabled());
    }
    
    @ModifyExpressionValue(method = "attack", at = @At(value = "INVOKE", target = "getYRot"))
    public float getYaw(float yaw) { return MotionE.get().yaw; }
}
