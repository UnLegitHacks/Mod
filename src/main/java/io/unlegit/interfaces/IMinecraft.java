package io.unlegit.interfaces;

import io.unlegit.UnLegit;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public interface IMinecraft
{
    Minecraft mc = Minecraft.getInstance();

    default void clientMessage(String message)
    {
        mc.gui.getChat().addMessage(Component.literal("\n" + UnLegit.PREFIX + message + "\n"));
    }
}
