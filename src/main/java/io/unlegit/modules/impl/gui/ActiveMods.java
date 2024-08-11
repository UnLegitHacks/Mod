package io.unlegit.modules.impl.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;

import com.mojang.blaze3d.platform.GlStateManager;

import io.unlegit.UnLegit;
import io.unlegit.events.impl.GuiRenderE;
import io.unlegit.gui.font.IFont;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.impl.ToggleSetting;

@IModule(name = "Active Mods", description = "Shows the enabled mods.")
public class ActiveMods extends ModuleU
{
    public ToggleSetting rainbowColor = new ToggleSetting("Rainbow Color", "Makes the modules a rainbow color.", false);
    private ArrayList<ModuleU> modules;
    
    public void onGuiRender(GuiRenderE e)
    {
        if (modules == null) { initializeModules(); }
        GlStateManager._enableBlend();
        GlStateManager._blendFuncSeparate(770, 771, 1, 1);
        int offset = 0;
        
        for (ModuleU module : modules)
        {
            if (!module.isEnabled()) continue;
            Color color = rainbowColor.enabled ? getSpectrum(offset) : Color.WHITE;
            int x = mc.getWindow().getGuiScaledWidth() - IFont.NORMAL.getStringWidth(module.name);
            IFont.NORMAL.drawStringWithShadow(e.graphics, module.name, x - 5, offset + 2, color);
            offset += 13;
        }
        
        GlStateManager._disableBlend();
    }
    
    public Color getSpectrum(int offset)
    {
        return Color.getHSBColor((((System.currentTimeMillis() / 25) + offset) % 255) / 255F, 0.75F, 1);
    }
    
    public void initializeModules()
    {
        modules = new ArrayList<>(UnLegit.modules.get());
        modules.sort(Comparator.comparingInt(module -> -IFont.NORMAL.getStringWidth(module.name)));
    }
}
