package io.unlegit.mixins.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.unlegit.UnLegit;
import io.unlegit.events.impl.entity.MotionE;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.impl.combat.Reach;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Player.class)
public class PlayerMixin
{
    @Unique private final Minecraft minecraft = Minecraft.getInstance();
    
    @Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;setSprinting(Z)V"))
    public void keepSprint(Player player, boolean bl)
    {
        ModuleU keepSprint = UnLegit.modules.get("Keep Sprint");
        if (!keepSprint.isEnabled() || !player.is(minecraft.player)) player.setSprinting(bl);
    }
    
    @Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;multiply(DDD)Lnet/minecraft/world/phys/Vec3;", ordinal = 0))
    private Vec3 slowVelocity(Vec3 vec3, double x, double y, double z)
    {
        ModuleU keepSprint = UnLegit.modules.get("Keep Sprint");
        return keepSprint.isEnabled() ? vec3 : vec3.multiply(x, y, z);
    }
    
    @ModifyExpressionValue(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getYRot()F"))
    public float getYaw(float yaw) { return MotionE.get().yaw; }

    @ModifyReturnValue(method = "entityInteractionRange", at = @At(value = "RETURN"))
    public double entityInteractionRange(double range)
    {
        Reach reach = (Reach) UnLegit.modules.get("Reach");
        return reach.isEnabled() ? reach.combatReach.value : range;
    }
}
