package io.unlegit.modules.impl.gui;

import java.util.ArrayList;
import java.util.Comparator;

import com.mojang.blaze3d.platform.GlStateManager;

import io.unlegit.UnLegit;
import io.unlegit.events.impl.render.GuiRenderE;
import io.unlegit.gui.font.IFont;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.impl.*;
import io.unlegit.utils.render.Colorer;

@IModule(name = "Active Mods", description = "Shows the enabled mods.")
public class ActiveMods extends ModuleU
{
    public ColorSetting color = new ColorSetting("Color", "The color of the active mods.", 255, 255, 255, 255);
    public ToggleSetting background = new ToggleSetting("Background", "The background for the modules.", false);
    
    public ModeSetting position = new ModeSetting("Position", "The position for active mods.", new String[]
    {
        "Top Right", "Top Left", "Bottom Right", "Bottom Left"
    });
    
    private String prevPosition = position.selected;
    private ArrayList<ModuleU> modules;
    
    public void onGuiRender(GuiRenderE e)
    {
        checkModules();
        GlStateManager._enableBlend();
        GlStateManager._blendFuncSeparate(770, 771, 1, 1);
        int offset = 0;
        
        if (position.selected.startsWith("Top"))
        {
            for (ModuleU module : modules)
            {
                if (!module.isEnabled() || module.hidden) continue;
                
                int color = this.color.get();
                int x = mc.getWindow().getGuiScaledWidth() - IFont.NORMAL.getStringWidth(module.name) + ("Vanilla".equals(UnLegit.THEME) ? 1 : 0);
                
                if (background.enabled)
                {
                    if (position.equals("Top Right"))
                        e.graphics.fill(x - 8, offset + (offset == 0 ? 0 : 2), x + IFont.NORMAL.getStringWidth(module.name), offset + 15, Colorer.RGB(0, 0, 0, 128));
                    else
                        e.graphics.fill(0, offset + (offset == 0 ? 0 : 2), IFont.NORMAL.getStringWidth(module.name) + 8, offset + 15, Colorer.RGB(0, 0, 0, 128));
                }
                
                IFont.NORMAL.drawStringWithShadow(e.graphics, module.name, position.equals("Top Right") ? (x - 5) : 3, offset + 2, color);
                offset += 13;
            }
        }
        
        else
        {
            for (ModuleU module : modules)
            {
                if (!module.isEnabled() || module.hidden) continue;
                
                int color = this.color.get();
                int x = mc.getWindow().getGuiScaledWidth() - IFont.NORMAL.getStringWidth(module.name) + ("Vanilla".equals(UnLegit.THEME) ? 1 : 0);
                
                if (background.enabled)
                {
                    if (position.equals("Bottom Right"))
                        e.graphics.fill(x - 8, mc.getWindow().getGuiScaledHeight() - offset - (offset == 0 ? 0 : 2), x + IFont.NORMAL.getStringWidth(module.name), mc.getWindow().getGuiScaledHeight() - offset - 15, Colorer.RGB(0, 0, 0, 128));
                    else
                        e.graphics.fill(0, mc.getWindow().getGuiScaledHeight() - offset - (offset == 0 ? 0 : 2), IFont.NORMAL.getStringWidth(module.name) + 8, mc.getWindow().getGuiScaledHeight() - offset - 15, Colorer.RGB(0, 0, 0, 128));
                }
                
                IFont.NORMAL.drawStringWithShadow(e.graphics, module.name, position.equals("Bottom Right") ? (x - 5) : 3, mc.getWindow().getGuiScaledHeight() - offset - 13, color);
                offset += 13;
            }
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
        
        return offset + 13;
    }
    
    public void checkModules()
    {
        if (modules == null || !prevPosition.equals(position.selected))
        {
            modules = new ArrayList<>(UnLegit.modules.get());
            modules.sort(Comparator.comparingInt(module -> -IFont.NORMAL.getStringWidth(module.name)));
            prevPosition = position.selected;
        }
    }
    
    public ActiveMods() { hidden = true; }
}
