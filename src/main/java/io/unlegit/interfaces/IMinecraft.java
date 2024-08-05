package io.unlegit.interfaces;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;

public interface IMinecraft
{
    public Minecraft mc = Minecraft.getInstance();
    public LocalPlayer player = mc.player;
    public ClientLevel level = mc.level;
    public ClientPacketListener connection = mc.getConnection();
}
