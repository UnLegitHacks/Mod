package io.unlegit.mixins.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;
import io.unlegit.UnLegit;
import io.unlegit.modules.impl.render.ESP;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.PlayerTeam;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRenderMixin
{
    @Inject(method = "renderEntity", at = @At(value = "HEAD"))
    public void chamsBegin(Entity entity, double x, double y, double z, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, CallbackInfo info)
    {
        ESP esp = (ESP) UnLegit.modules.get("ESP");
        
        if (esp.isEnabled() && entity instanceof LivingEntity && !(entity instanceof LocalPlayer))
        {
            if (esp.mode.equals("Chams"))
            {
                GlStateManager._enablePolygonOffset();
                GlStateManager._polygonOffset(1, -1000000);
            }

            else if (entity.getType() == EntityType.PLAYER && multiBufferSource instanceof OutlineBufferSource)
            {
                Player player = (Player) entity;
                PlayerTeam team = player.getTeam();

                if (team != null && team.getColor().getColor() != null)
                {
                    int hexColor = team.getColor().getColor();
                    int red = (hexColor / 65536) % 256, blue = hexColor % 256, green = (hexColor / 256) % 256;
                    ((OutlineBufferSource) multiBufferSource).setColor(red, green, blue, 255);
                }
            }
        }
    }
    
    @Inject(method = "renderEntity", at = @At(value = "TAIL"))
    public void chamsEnd(Entity entity, double x, double y, double z, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, CallbackInfo info)
    {
        ESP esp = (ESP) UnLegit.modules.get("ESP");
        
        if (esp.isEnabled() && entity instanceof LivingEntity && !(entity instanceof LocalPlayer))
        {
            if (esp.mode.equals("Chams"))
            {
                GlStateManager._polygonOffset(1, 1000000);
                GlStateManager._disablePolygonOffset();
            }
        }
    }
}
