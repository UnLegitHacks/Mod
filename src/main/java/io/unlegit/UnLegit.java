package io.unlegit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.unlegit.events.EventBus;
import io.unlegit.events.EventListener;
import io.unlegit.events.impl.KeyE;
import io.unlegit.modules.ModuleManager;
import io.unlegit.modules.ModuleU;
import io.unlegit.utils.SoundUtil;
import net.fabricmc.api.ModInitializer;
import net.minecraft.ChatFormatting;

public class UnLegit implements ModInitializer, EventListener
{
    public static final Logger LOGGER = LoggerFactory.getLogger("unlegit");
    public static final String NAME = "UnLegit 3.0", PREFIX = getPrefix();
    public static ModuleManager modules;
    public static EventBus events;
    
    /**
     * Loads the client.
     */
    public void onInitialize()
    {
        events = new EventBus();
        modules = new ModuleManager();
        events.register(this);
        // Extracts the sound files.
        SoundUtil.playEnableSound(); SoundUtil.playDisableSound();
    }
    
    public void onKey(KeyE e)
    {
        for (ModuleU module : modules.get())
        {
            if (module.key == e.key) module.toggle();
        }
    }
    
    private static String getPrefix()
    {
        return ChatFormatting.AQUA + "UnLegit " + ChatFormatting.DARK_PURPLE + "> " + ChatFormatting.RESET;
    }
}