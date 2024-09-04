package io.unlegit.modules.impl.player;

import static org.lwjgl.glfw.GLFW.glfwGetKey;

import io.unlegit.interfaces.IModule;
import io.unlegit.mixins.client.AccKeyMap;
import io.unlegit.modules.ModuleU;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.ChatScreen;

@IModule(name = "Inv Move", description = "Allows you to move while a container is open.")
public class InvMove extends ModuleU
{
    public boolean canMove(KeyMapping key)
    {
        return mc.screen != null && !(mc.screen instanceof ChatScreen) && !key.equals(mc.options.keyShift) ?
                glfwGetKey(mc.getWindow().getWindow(), ((AccKeyMap) key).getKey()
                        .getValue()) == 1 : key.isDown();
    }
}
