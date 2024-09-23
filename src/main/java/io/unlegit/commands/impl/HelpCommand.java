package io.unlegit.commands.impl;

import io.unlegit.UnLegit;
import io.unlegit.commands.Command;
import io.unlegit.interfaces.ICommand;
import net.minecraft.ChatFormatting;

@ICommand(name = "Help", shortForm = "h", usage = ".help")
public class HelpCommand extends Command
{
    public void onExecute(String[] args)
    {
        StringBuilder commands = new StringBuilder(
                ChatFormatting.LIGHT_PURPLE + "Commands:\n");
        
        for (Command command : UnLegit.commands.get())
        {
            if (equals(command)) continue;
            
            commands.append("\n").append(ChatFormatting.YELLOW).append(
                    command.name).append(": ").append(ChatFormatting.DARK_GREEN).append(command.usage);
        }
        
        clientMessage(commands.toString());
    }
}
