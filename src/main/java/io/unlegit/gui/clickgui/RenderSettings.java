package io.unlegit.gui.clickgui;

import java.awt.Color;

import io.unlegit.gui.font.IFont;
import io.unlegit.interfaces.IMinecraft;
import io.unlegit.modules.ModuleU;
import io.unlegit.utils.render.Animation;
import net.minecraft.client.gui.GuiGraphics;

public class RenderSettings implements IMinecraft
{
    protected Animation animation = null;
    private int width, height;
    private ModuleU module;
    
    public RenderSettings(ModuleU module)
    {
        this.module = module;
        width = mc.getWindow().getGuiScaledWidth();
        height = mc.getWindow().getGuiScaledHeight();
    }
    
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks)
    {
        int wD = width / 2, hD = height / 2;
        graphics.fill(wD - 150, hD - 125, wD + 150, hD + 125, new Color(10, 10, 10, 200).getRGB());
        
        graphics.fill(wD - 150, hD - 155, wD - 145 + IFont.LARGE.getStringWidth(module.name) + 10, hD - 125, new Color(10, 10, 10, 200).getRGB());
        IFont.LARGE.drawString(graphics, module.name, wD - 143, hD - 152, Color.WHITE);
        IFont.NORMAL.drawString(graphics, module.description, wD - 145, hD - 122, new Color(235, 235, 235));
    }
    
    public void onClose()
    {
        ClickGui.get().renderSettings = null;
    }
}
