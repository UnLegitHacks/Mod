package io.unlegit.modules.impl.gui.keystrokes;

import java.util.ArrayList;

import io.unlegit.events.impl.render.GuiRenderE;
import io.unlegit.gui.UnLegitOptions;
import io.unlegit.interfaces.IGui;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

@IModule(name = "Key Strokes", description = "Shows your key strokes on the screen.")
public class KeyStrokes extends ModuleU implements IGui
{
    private GuiGraphics dummy = new GuiGraphics(mc, null);
    public ArrayList<RenderKey> keys = new ArrayList<>();
    protected ResourceLocation keyShadow, clickShadow;
    private float partialTicks = 0;
    
    public void onGuiRender(GuiRenderE e)
    {
        partialTicks = e.partialTicks;
        
        if (keys.isEmpty())
        {
            int y = UnLegitOptions.WATER_MARK ? 32 : 0;
            
            add(new RenderKey(this, mc.options.keyUp, 24, y),
                new RenderKey(this, mc.options.keyLeft, -2, y + 26),
                new RenderKey(this, mc.options.keyDown, 24, y + 26),
                new RenderKey(this, mc.options.keyRight, 50, y + 26),
                new RenderClick(this, mc.options.keyAttack, -2, y + 52),
                new RenderClick(this, mc.options.keyUse, 37, y + 52));
            
            keyShadow = withLinearScaling(ResourceLocation.fromNamespaceAndPath("unlegit", "modules/keystrokes/key.png"));
            clickShadow = withLinearScaling(ResourceLocation.fromNamespaceAndPath("unlegit", "modules/keystrokes/click.png"));
        }
        
        for (RenderKey key : keys) key.render(e.graphics, partialTicks);
        e.graphics.fill(0, 0, 0, 0, 0);
    }
    
    public void onUpdate()
    {
        // Fixes the edges of the blur
        if (mc.screen == null)
        {
            for (RenderKey key : keys) key.renderBlur(dummy, getBlurriness() * 2, partialTicks);
        }
    }
    
    public void add(RenderKey... keys)
    {
        for (RenderKey key : keys) this.keys.add(key);
    }
}
