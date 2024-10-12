package io.unlegit.mixins.gui;

import java.awt.Color;

import org.spongepowered.asm.mixin.*;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import io.unlegit.gui.font.IFont;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LogoRenderer;

@Mixin(LogoRenderer.class)
public class LogoRenderMixin
{
    @Shadow @Final private boolean keepLogoThroughFade;
    
    @Overwrite
    public void renderLogo(GuiGraphics guiGraphics, int i, float f, int j)
    {
        PoseStack pose = guiGraphics.pose();
        guiGraphics.setColor(1, 1, 1, keepLogoThroughFade ? 1 : f);
        RenderSystem.enableBlend();
        
        pose.pushPose();
        pose.scale(5, 5, 5);
        IFont.NORMAL.drawCenteredStringWithShadow(guiGraphics, "UnLegit", i / 10, (j + 12) / 5, Color.CYAN.getRGB());
        pose.popPose();
        
        guiGraphics.setColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
    }
}
