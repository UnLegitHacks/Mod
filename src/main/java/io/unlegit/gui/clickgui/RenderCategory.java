package io.unlegit.gui.clickgui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;

import org.apache.commons.lang3.StringUtils;

import io.unlegit.UnLegit;
import io.unlegit.gui.font.IFont;
import io.unlegit.interfaces.IGui;
import io.unlegit.modules.CategoryM;
import io.unlegit.modules.ModuleU;
import net.minecraft.client.gui.GuiGraphics;

public class RenderCategory implements IGui
{
    private int x, y, prevX, prevY, prevMouseX, prevMouseY, scroll;
    private ArrayList<ModuleU> modules;
    private boolean dragging = false;
    private CategoryM category;
    
    public RenderCategory(CategoryM category, int x, int y)
    {
        this.category = category;
        this.x = x;
        this.y = y;
        modules = UnLegit.modules.get(category);
        modules.sort(Comparator.comparing(module -> module.name));
    }
    
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks)
    {
        if (dragging)
        {
            x = prevX + (mouseX - prevMouseX);
            y = prevY + (mouseY - prevMouseY);
        }
        
        ClickGui parent = ClickGui.get();
        float scale = 1 + (2 - (parent.animation.get() * 2));
        int alpha = parent.animation.wrap(255);
        int x = (int) (this.x - ((scale - 1) * 220));
        int y = (int) (this.y - ((scale - 1) * 82.5F));
        graphics.fill(x, y, x + 110, y + 30, new Color(10, 10, 10, parent.animation.wrap(235)).getRGB());
        String name = StringUtils.capitalize(category.name().toLowerCase());
        IFont.BIG.drawString(graphics, name, x + 10, y + 6, new Color(192, 192, 192, alpha));
        graphics.fill(x, y + 30, x + 110, y + 160, new Color(0, 0, 0, alpha).getRGB());
        graphics.enableScissor(x, y + (int) (30 * scale), x + (int) (110 * scale), y + (int) (160 * scale));
        int offset = scroll;
        
        for (ModuleU module : modules)
        {
            if (module.isEnabled())
            {
                if (mouseOver(mouseX, mouseY, x, y + 30 + offset, x + 110, y + 45 + offset))
                    graphics.fill(x, y + 30 + offset, x + 110, y + 45 + offset, new Color(0, 175, 255, alpha).getRGB());
                else
                    graphics.fill(x, y + 30 + offset, x + 110, y + 45 + offset, new Color(0, 150, 255, alpha).getRGB());
            } else if (mouseOver(mouseX, mouseY, x, y + 30 + offset, x + 110, y + 45 + offset))
                graphics.fill(x, y + 30 + offset, x + 110, y + 45 + offset, new Color(24, 24, 24, alpha).getRGB());
            
            IFont.NORMAL.drawString(graphics, module.name, x + (module.isEnabled() ? 15 : 10), y + 32 + offset, module.isEnabled() ? new Color(0, 0, 0, alpha) : new Color(255, 255, 255, alpha));
            offset += 15;
        }
        
        graphics.disableScissor();
        graphics.setColor(1, 1, 1, alpha / 255F);
//        mc.getTextureManager().bindTexture(new ResourceLocation("unlegit/shadow/category/maximized.png"));
//        drawModalRectWithCustomSizedTexture(x - 5.25F, y - 5.25F, 0, 0, 362 / 3F, 527 / 3F, 362 / 3F, 527 / 3F);
    }
    
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        if (mouseOver((int) mouseX, (int) mouseY, x, y, x + 110, y + 30))
        {
            dragging = true;
            prevX = x; prevY = y;
            prevMouseX = (int) mouseX; prevMouseY = (int) mouseY;
            return true;
        }
        
        else
        {
            if (mouseOver((int) mouseX, (int) mouseY, x, y + 30, x + 110, y + 165))
            {
                int offset = scroll;
                
                for (ModuleU module : modules)
                {
                    if (mouseOver((int) mouseX, (int) mouseY, x, y + 30 + offset, x + 110, y + 45 + offset))
                    {
                        if (button == 0) module.toggle();
                        //else parent.renderSettings = new RenderSettings(module);
                        return true;
                    }
                    
                    offset += 15;
                }
            }
        }
        
        return false;
    }
    
    public boolean mouseReleased(double mouseX, double mouseY, int button)
    {
        if (dragging) { dragging = false; return true; }
        else return false;
    }
    
    public boolean mouseScrolled(double d, double e, double f, double g)
    {
        int mouseX = (int) d;
        int mouseY = (int) e;
        
        if (mouseOver(mouseX, mouseY, x, y + 30, x + 110, y + 165))
        {
            int event = (int) g;
            
            if (event != 0 && modules.size() > 8)
            {
                if (event < 0) event = -1;
                if (event > 0) event = 1;
                scroll += event * 15;
                if (scroll > 0) scroll = 0;
            }
            
            return true;
        }
        
        return false;
    }
}
