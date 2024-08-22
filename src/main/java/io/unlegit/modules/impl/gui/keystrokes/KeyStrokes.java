package io.unlegit.modules.impl.gui.keystrokes;

import java.util.ArrayList;

import io.unlegit.events.impl.render.GuiRenderE;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;

@IModule(name = "Key Strokes", description = "Shows your key strokes on the screen.")
public class KeyStrokes extends ModuleU
{
    private ArrayList<RenderKey> keys = new ArrayList<>();
    
    public void onGuiRender(GuiRenderE e)
    {
        if (keys.isEmpty())
            add(new RenderKey(mc.options.keyUp, 24, 0),
                new RenderKey(mc.options.keyLeft, -2, 26),
                new RenderKey(mc.options.keyDown, 24, 26),
                new RenderKey(mc.options.keyRight, 50, 26),
                new RenderClick(mc.options.keyAttack, -2, 52),
                new RenderClick(mc.options.keyUse, 37, 52));
        
        for (RenderKey key : keys)
            key.render(e.graphics, e.partialTicks);
    }
    
    public void add(RenderKey... keys)
    {
        for (RenderKey key : keys) this.keys.add(key);
    }
}
