package io.unlegit.commands.impl;

import io.unlegit.commands.Command;
import io.unlegit.interfaces.ICommand;

@ICommand(name = "Say", shortForm = "S", usage = ".say <message>")
public class SayCommand extends Command
{
    public void onExecute(String[] args)
    {
        if (!validateArgs(args, 1)) return;
        mc.getConnection().sendChat(String.join(" ", args));
    }
}
