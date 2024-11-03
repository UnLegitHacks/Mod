package io.unlegit.mixins.gui;

import java.awt.Color;

import io.unlegit.utils.render.Colorer;
import net.minecraft.util.ARGB;
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

    /**
     * @author You
     * @reason Renders the UnLegit logo.
     */
    @Overwrite
    public void renderLogo(GuiGraphics graphics, int i, float f, int j)
    {
        float g = keepLogoThroughFade ? 1 : f, l = ARGB.white(g);
        PoseStack pose = graphics.pose();
        RenderSystem.enableBlend();
        
        pose.pushPose();
        pose.scale(5, 5, 5);
        IFont.NORMAL.drawCenteredStringWithShadow(graphics, "UnLegit", i / 10, (j + 12) / 5, Colorer.RGB(0, 255, 255, l));
        pose.popPose();

        RenderSystem.disableBlend();
    }
}
