package io.unlegit.gui.clickgui;

import java.awt.Color;
import java.util.ArrayList;

import com.mojang.blaze3d.vertex.PoseStack;

import io.unlegit.config.UnConfig;
import io.unlegit.interfaces.IGui;
import io.unlegit.mixins.render.AccGameRender;
import io.unlegit.modules.CategoryM;
import io.unlegit.utils.render.Animation;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ClickGui extends Screen implements IGui
{
    protected final ResourceLocation categoryShadow = withLinearScaling(ResourceLocation.fromNamespaceAndPath("unlegit", "clickgui/category_shadow.png"));
    private ArrayList<RenderCategory> categories = new ArrayList<>();
    private static ClickGui INSTANCE = new ClickGui();
    protected RenderSettings renderSettings = null;
    protected Animation animation = null;
    private boolean closingGui = false;
    
    public ClickGui()
    {
        super(Component.literal("Click Gui"));
        int y = 10, x = 10, i = 0;
        
        for (CategoryM category : CategoryM.values())
        {
            if (i != 0 && i % 4 == 0)
            {
                y += 168;
                x = 10;
            }
            
            categories.add(new RenderCategory(category, x, y, this));
            x += 118; i++;
        }
    }
    
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks)
    {
        renderBlurredBackground(partialTicks);
        PoseStack poseStack = graphics.pose();
        float scale = 1 + (1 - animation.get());
        graphics.fill(0, 0, width, height, new Color(0, 0, 0, animation.wrap(50)).getRGB());
        
        if (!animation.finished())
        {
            poseStack.pushPose();
            poseStack.scale(scale, scale, scale);
        } else if (closingGui) minecraft.setScreen(null);
        
        if (renderSettings != null)
        {
            for (RenderCategory category : categories)
                category.render(graphics, 0, 0, partialTicks);
//            
//            renderSettings.drawScreen(mouseX, mouseY, partialTicks);
        }
        
        else
        {
            for (RenderCategory category : categories)
                category.render(graphics, mouseX, mouseY, partialTicks);
        }
        
        if (!animation.finished()) poseStack.popPose();
    }
    
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        for (RenderCategory category : categories)
            if (category.mouseClicked(mouseX, mouseY, button)) break;
        
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    public boolean mouseReleased(double mouseX, double mouseY, int button)
    {
        for (RenderCategory category : categories)
            if (category.mouseReleased(mouseX, mouseY, button)) break;
        
        return super.mouseReleased(mouseX, mouseY, button);
    }
    
    public boolean mouseScrolled(double d, double e, double f, double g)
    {
        for (RenderCategory category : categories)
            if (category.mouseScrolled(d, e, f, g)) break;
        
        return super.mouseScrolled(d, e, f, g);
    }
    
    protected void init()
    {
        closingGui = false; animation = new Animation(96);
    }
    
    // Exists to control the blur fade-in and fade-out.
    protected void renderBlurredBackground(float partialTicks)
    {
        PostChain blurEffect = ((AccGameRender) minecraft.gameRenderer).getBlurEffect();
        float blurriness = minecraft.options.getMenuBackgroundBlurriness();
        
        if (blurEffect != null && blurriness >= 1)
        {
            blurEffect.setUniform("Radius", blurriness * animation.get());
            blurEffect.process(partialTicks);
        }
        
        minecraft.getMainRenderTarget().bindWrite(false);
    }
    
    public void onClose()
    {
        // if (renderSettings != null) renderSettings.onClose();
        animation = new Animation(96); animation.reverse = true;
        UnConfig.saveModules();
        closingGui = true;
    }
    
    public boolean isPauseScreen() { return false; }
    public static ClickGui get() { return INSTANCE; }
}
