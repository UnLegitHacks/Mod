package io.unlegit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.unlegit.events.EventBus;
import net.fabricmc.api.ModInitializer;

public class UnLegit implements ModInitializer
{
    public static final Logger LOGGER = LoggerFactory.getLogger("unlegit");
    public static final String NAME = "UnLegit 3.0";
    public static EventBus eventBus;
    
    /**
     * Loads the client.
     */
    public void onInitialize()
    {
        eventBus = new EventBus();
    }
}