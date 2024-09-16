package io.unlegit.commands.impl;

import io.unlegit.UnLegit;
import io.unlegit.commands.Command;
import io.unlegit.events.EventListener;
import io.unlegit.gui.UnInventoryScreen;
import io.unlegit.interfaces.ICommand;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

@ICommand(name = "InvSee", shortForm = "is", exampleUse = ".invsee <player>")
public class InvSeeCommand extends Command implements EventListener
{
    private Screen screen = null;
    
    public void onExecute(String[] args)
    {
        if (!validateArgs(args, 1)) return;
        Player player = null;
        
        for (Entity entity : mc.level.entitiesForRendering())
        {
            if (entity instanceof Player entityPlayer)
            {
                String name = entityPlayer.getName().getString();
                
                if (name.equalsIgnoreCase(args[0]))
                {
                    player = entityPlayer;
                    break;
                }
            }
        }
        
        if (player != null)
        {
            UnLegit.events.register(this);
            screen = new UnInventoryScreen(player);
            clientMessage(UnLegit.PREFIX + ChatFormatting.GREEN + "Showing the inventory of " + player.getName().getString() + ".");
        }
        
        else clientMessage(UnLegit.PREFIX + ChatFormatting.DARK_RED + "Player " + args[0] + " not found!");
    }
    
    public void onUpdate()
    {
        if (screen != null)
        {
            mc.setScreen(screen);
            screen = null;
            UnLegit.events.unregister(this);
        }
    }
}
