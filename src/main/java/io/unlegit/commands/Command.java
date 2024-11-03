package io.unlegit.commands;

import io.unlegit.interfaces.ICommand;
import io.unlegit.interfaces.IMinecraft;
import net.minecraft.ChatFormatting;

public abstract class Command implements IMinecraft
{
    private ICommand iCommand = getClass().getAnnotation(ICommand.class);
    public String name = iCommand.name(), shortForm = iCommand.shortForm(), usage = iCommand.usage();
    public abstract void onExecute(String[] args);

    protected boolean validateArgs(String[] args, int num)
    {
        if (args == null || args.length != num)
        {
            clientMessage(ChatFormatting.YELLOW + usage);
            return false;
        }

        return true;
    }
}
