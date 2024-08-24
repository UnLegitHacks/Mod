package io.unlegit.modules.impl.gui.keystrokes;

import java.util.ArrayList;

import io.unlegit.events.impl.render.GuiRenderE;
import io.unlegit.interfaces.IGui;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

@IModule(name = "Key Strokes", description = "Shows your key strokes on the screen.")
public class KeyStrokes extends ModuleU implements IGui
{
    private ArrayList<RenderKey> keys = new ArrayList<>();
    private GuiGraphics dummy = new GuiGraphics(mc, null);
    protected ResourceLocation keyShadow, clickShadow;
    
    public void onGuiRender(GuiRenderE e)
    {
        if (keys.isEmpty())
        {
            add(new RenderKey(this, mc.options.keyUp, 24, 0),
                new RenderKey(this, mc.options.keyLeft, -2, 26),
                new RenderKey(this, mc.options.keyDown, 24, 26),
                new RenderKey(this, mc.options.keyRight, 50, 26),
                new RenderClick(this, mc.options.keyAttack, -2, 52),
                new RenderClick(this, mc.options.keyUse, 37, 52));
            
            keyShadow = withLinearScaling(ResourceLocation.fromNamespaceAndPath("unlegit", "modules/keystrokes/key.png"));
            clickShadow = withLinearScaling(ResourceLocation.fromNamespaceAndPath("unlegit", "modules/keystrokes/click.png"));
        }
        
        for (RenderKey key : keys) key.render(e.graphics, e.partialTicks);
    }
    
    public void onUpdate()
    {
        // Fixes the edges of the blur
        for (RenderKey key : keys) key.renderBlur(dummy, mc.options.getMenuBackgroundBlurriness() * 2, mc.getTimer().getGameTimeDeltaPartialTick(false));
    }
    
    public void add(RenderKey... keys)
    {
        for (RenderKey key : keys) this.keys.add(key);
    }
}
