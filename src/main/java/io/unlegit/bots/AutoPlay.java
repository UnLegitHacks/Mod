package io.unlegit.bots;

import io.unlegit.bots.impl.SkywarsBot;
import io.unlegit.bots.impl.SumoBot;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.impl.ModeSetting;

/**
 * Not to be confused with Game Play,
 * which is not related to this module.
 */
@IModule(name = "Auto Play", description = "Automatically plays a minigame.")
public class AutoPlay extends ModuleU
{
    public ModeSetting game = new ModeSetting("Game", "The minigame for the bot to play.", new String[]
    {
        "Sumo", "Skywars"
    });

    private Bot bot;

    public void onEnable()
    {
        super.onEnable();

        if (mc.hasSingleplayerServer())
        {
            super.onDisable();
            return;
        }

        switch (game.selected)
        {
            case "Skywars":
                bot = new SkywarsBot();
                break;
            case "Sumo":
                bot = new SumoBot();
                break;
        }

        bot.onEnable();
    }

    public void onDisable()
    {
        super.onDisable();
        bot.onDisable();
    }

    public AutoPlay() { noStart = true; }
}
