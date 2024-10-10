package io.unlegit.commands.impl;

import io.unlegit.commands.Command;
import io.unlegit.interfaces.ICommand;

@ICommand(name = "Say", shortForm = "S", usage = ".say <message>")
public class SayCommand extends Command
{
    public void onExecute(String[] args)
    {
        String message = String.join(" ", args);
        mc.getConnection().sendChat(message.startsWith("/") ? message.substring(1) : message);
    }
}
