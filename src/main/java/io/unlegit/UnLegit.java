package io.unlegit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mojang.blaze3d.platform.InputConstants;

import io.unlegit.commands.CommandManager;
import io.unlegit.config.UnConfig;
import io.unlegit.events.EventBus;
import io.unlegit.events.EventListener;
import io.unlegit.events.impl.client.KeyE;
import io.unlegit.events.impl.render.GuiRenderE;
import io.unlegit.gui.UnLegitOptions;
import io.unlegit.gui.clickgui.ClickGui;
import io.unlegit.gui.font.IFont;
import io.unlegit.interfaces.IMinecraft;
import io.unlegit.modules.ModuleManager;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.SettingManager;
import io.unlegit.utils.SoundUtil;
import io.unlegit.utils.render.Colorer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.ChatFormatting;

public class UnLegit implements ModInitializer, EventListener, IMinecraft
{
    public static final String VERSION = "3.0", NAME = "UnLegit " + VERSION, PREFIX = getPrefix();
    public static final Logger LOGGER = LoggerFactory.getLogger("UnLegit");
    private static boolean firstLaunch = false;
    public static String THEME = "Fancy";
    
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
        if (e.key == InputConstants.KEY_RSHIFT)
            mc.setScreen(ClickGui.get());
        else for (ModuleU module : modules.get())
        {
            if (module.key == e.key) module.toggle();
        }
    }
    
    public void onGuiRender(GuiRenderE e)
    {
        if (UnLegitOptions.WATER_MARK)
        {
            IFont.LARGE.drawStringWithShadow(e.graphics, "UnLegit", 3, 2, Colorer.RGB(255, 255, 255, 128));
            IFont.NORMAL.drawStringWithShadow(e.graphics, VERSION, 4, 23, Colorer.RGB(255, 255, 255, 128));
        }
    }
    
    private static String getPrefix()
    {
        return ChatFormatting.DARK_PURPLE + "> " + ChatFormatting.RESET;
    }
    
    public static boolean isFirstLaunch() { return firstLaunch; }
    public static void setFirstLaunch(boolean firstLaunch) { UnLegit.firstLaunch = firstLaunch; }
}