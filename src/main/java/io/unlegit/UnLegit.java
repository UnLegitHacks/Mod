package io.unlegit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mojang.blaze3d.platform.InputConstants;

import io.unlegit.commands.CommandManager;
import io.unlegit.config.UnConfig;
import io.unlegit.events.EventBus;
import io.unlegit.events.EventListener;
import io.unlegit.events.impl.client.KeyE;
import io.unlegit.events.impl.network.PacketSendE;
import io.unlegit.gui.clickgui.ClickGui;
import io.unlegit.interfaces.IMinecraft;
import io.unlegit.modules.ModuleManager;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.SettingManager;
import io.unlegit.utils.SoundUtil;
import net.fabricmc.api.ModInitializer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.protocol.game.ServerboundChatPacket;

public class UnLegit implements ModInitializer, EventListener, IMinecraft
{
    public static final String NAME = "UnLegit 3.0", PREFIX = getPrefix(), THEME = "Fancy";
    public static final Logger LOGGER = LoggerFactory.getLogger("UnLegit");
    private static boolean firstLaunch = false;
    
    public static SettingManager settings;
    public static CommandManager commands;
    public static ModuleManager modules;
    public static EventBus events;
    
    /** Loads the client. */
    public void onInitialize()
    {
        events = new EventBus();
        commands = new CommandManager();
        settings = new SettingManager();
        modules = new ModuleManager();
        
        setFirstLaunch(!UnConfig.config.exists());
        events.register(this);
        UnConfig.init();
        
        // Fixes an issue.
        SoundUtil.playActionSound();
        LOGGER.info("Successfully loaded up.");
    }
    
    public void onKey(KeyE e)
    {
        if (e.key == InputConstants.KEY_RSHIFT) mc.setScreen(ClickGui.get());
        else for (ModuleU module : modules.get())
        {
            if (module.key == e.key) module.toggle();
        }
    }
    
    public void onPacketSend(PacketSendE e)
    {
        if (e.packet instanceof ServerboundChatPacket packet && packet.message().startsWith("."))
        {
            commands.handle(packet.message());
            e.cancelled = true;
        }
    }
    
    private static String getPrefix()
    {
        return ChatFormatting.AQUA + "UnLegit " + ChatFormatting.DARK_PURPLE + "> " + ChatFormatting.RESET;
    }
    
    public static boolean isFirstLaunch() { return firstLaunch; }
    public static void setFirstLaunch(boolean firstLaunch) { UnLegit.firstLaunch = firstLaunch; }
}