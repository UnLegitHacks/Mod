package io.unlegit.modules.impl.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;

import com.mojang.blaze3d.platform.GlStateManager;

import io.unlegit.UnLegit;
import io.unlegit.events.impl.render.GuiRenderE;
import io.unlegit.gui.font.IFont;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.impl.ToggleSetting;

@IModule(name = "Active Mods", description = "Shows the enabled mods.")
public class ActiveMods extends ModuleU
{
    public ToggleSetting rainbowColor = new ToggleSetting("Rainbow Color", "Makes the modules a rainbow color.", false),
                         background = new ToggleSetting("Background", "The background for the modules.", false);
    
    private ArrayList<ModuleU> modules;
    
    public void onGuiRender(GuiRenderE e)
    {
        checkModules();
        GlStateManager._enableBlend();
        GlStateManager._blendFuncSeparate(770, 771, 1, 1);
        int offset = 0;
        
        for (ModuleU module : modules)
        {
            if (!module.isEnabled() || module.hidden) continue;
            
            Color color = rainbowColor.enabled ? getSpectrum(offset) : Color.WHITE;
            int x = mc.getWindow().getGuiScaledWidth() - IFont.NORMAL.getStringWidth(module.name) + ("Vanilla".equals(UnLegit.THEME) ? 1 : 0);
            
            if (background.enabled)
                e.graphics.fill(x - 8, offset + (offset == 0 ? 0 : 2), x + IFont.NORMAL.getStringWidth(module.name), offset + 15, new Color(0, 0, 0, 128).getRGB());
            
            IFont.NORMAL.drawStringWithShadow(e.graphics, module.name, x - 5, offset + 2, color);
            offset += 13;
        }
        
        GlStateManager._disableBlend();
        e.graphics.fill(0, 0, 0, 0, 0);
    }
    
    public int getHeight()
    {
        checkModules();
        int offset = 0;
        
        for (ModuleU module : modules)
        {
            if (module.isEnabled() && !module.hidden) offset += 13;
        }
        
        return offset + 12;
    }
    
    public void checkModules()
    {
        if (modules == null)
        {
            modules = new ArrayList<>(UnLegit.modules.get());
            modules.sort(Comparator.comparingInt(module -> -IFont.NORMAL.getStringWidth(module.name)));
        }
    }
    
    public Color getSpectrum(int offset)
    {
        return Color.getHSBColor((((System.currentTimeMillis() / 25) + offset) % 255) / 255F, 0.75F, 1);
    }
}
