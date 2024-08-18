package io.unlegit.modules.impl.player;

import static org.lwjgl.glfw.GLFW.glfwGetKey;

import io.unlegit.interfaces.IModule;
import io.unlegit.mixins.client.KeyMapAccessor;
import io.unlegit.modules.ModuleU;
import io.unlegit.utils.entity.PlayerUtil;

@IModule(name = "Safe Walk", description = "Automatically sneaks when you are on edges.")
public class SafeWalk extends ModuleU
{
    private boolean down = false;
    
    public void onUpdate()
    {
        if (canSafeWalk())
        {
            mc.options.keyShift.setDown(true); down = true;
        }
        
        else if (down)
        {
            mc.options.keyShift.setDown(glfwGetKey(mc.getWindow().getWindow(),
                    ((KeyMapAccessor) mc.options.keyShift).getKey().getValue()) == 1);
            down = false;
        }
    }
    
    public boolean canSafeWalk()
    {
        return PlayerUtil.isCloseToEdge(mc.player.position());
    }
}
