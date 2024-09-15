package io.unlegit.commands.impl;

import io.unlegit.UnLegit;
import io.unlegit.commands.Command;
import io.unlegit.interfaces.ICommand;
import net.minecraft.ChatFormatting;

@ICommand(name = "VClip", shortForm = "vc", exampleUse = ".vclip <height>")
public class VClipCommand extends Command
{
    public void onExecute(String[] args)
    {
        if (!validateArgs(args, 1)) return;
        int height = 0;
        
        try { height = Integer.parseInt(args[0]); }
        catch (NumberFormatException e)
        {
            clientMessage(UnLegit.PREFIX + ChatFormatting.DARK_RED + "The height is not an integer!");
            return;
        }
        
        mc.player.setPos(mc.player.getX(), mc.player.getY() + height, mc.player.getZ());
        clientMessage(UnLegit.PREFIX + ChatFormatting.GREEN + "Teleported you " + height + " blocks vertically.");
    }
}
