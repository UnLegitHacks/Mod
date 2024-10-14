package io.unlegit.gui.clickgui;

import java.util.ArrayList;

import com.mojang.blaze3d.vertex.PoseStack;

import io.unlegit.config.UnConfig;
import io.unlegit.gui.clickgui.category.RenderCategory;
import io.unlegit.interfaces.IGui;
import io.unlegit.modules.CategoryM;
import io.unlegit.utils.render.Animation;
import io.unlegit.utils.render.Colorer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ClickGui extends Screen implements IGui
{
    public final ResourceLocation categoryShadow = withLinearScaling(ResourceLocation.fromNamespaceAndPath("unlegit", "clickgui/category_shadow.png"));
    private ArrayList<RenderCategory> categories = new ArrayList<>();
    private static ClickGui INSTANCE = new ClickGui();
    public RenderSettings renderSettings = null;
    private boolean closingGui = false;
    public Animation animation = null;
    
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
        graphics.fill(0, 0, width, height, Colorer.RGB(0, 0, 0, animation.wrap(64)));
        boolean finished = animation.finished();
        
        if (!finished)
        {
            poseStack.pushPose();
            poseStack.scale(scale, scale, scale);
        } else if (closingGui) minecraft.setScreen(null);
        
        if (renderSettings != null)
        {
            for (RenderCategory category : categories)
                category.render(graphics, 0, 0, partialTicks);
            
            renderSettings.render(graphics, mouseX, mouseY, partialTicks);
        }
        
        else
        {
            for (RenderCategory category : categories)
                category.render(graphics, mouseX, mouseY, partialTicks);
        }
        
        if (!finished) poseStack.popPose();
    }
    
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        if (renderSettings != null) return super.mouseClicked(mouseX, mouseY, button);
        
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
        animation = new Animation(96); closingGui = false;
    }
    
    // Exists to control the blur fade-in and fade-out.
    protected void renderBlurredBackground(float partialTicks)
    {
        blur(animation.get(), partialTicks);
    }
    
    public void onClose()
    {
        if (renderSettings != null) renderSettings.onClose();
        animation = new Animation(96); animation.reverse = true;
        closingGui = true;
        UnConfig.save();
    }
    
    public int brightBluple(int x, int alpha)
    {
        return Colorer.blend(x / (float) width, Colorer.RGB(0, 175, 255, alpha), Colorer.RGB(200, 125, 255, alpha));
    }
    
    public int normalBluple(int x, int alpha)
    {
        return Colorer.blend(x / (float) width, Colorer.RGB(0, 150, 255, alpha), Colorer.RGB(200, 100, 255, alpha));
    }
    
    public boolean isPauseScreen() { return false; }
    public static ClickGui get() { return INSTANCE; }
}
