package io.unlegit.commands;

import io.unlegit.commands.impl.*;
import io.unlegit.interfaces.IMinecraft;
import net.minecraft.ChatFormatting;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandManager implements IMinecraft
{
    private final ArrayList<Command> commands = new ArrayList<>();
    
    public CommandManager()
    {
        add(new ToggleCommand(), new BindCommand(), new InvSeeCommand(),
            new VClipCommand(), new HideCommand(), new HelpCommand(),
            new SayCommand());
    }
    
    public void handle(String message)
    {
        String[] segments = message.contains(" ") ? message.split(" ") : new String[] {message};
        String name = segments[0].substring(1);
        Command command = getCommand(name);
        
        if (command != null)
            command.onExecute(segments.length == 1 ? null : Arrays.copyOfRange(segments, 1, segments.length));
        else clientMessage(ChatFormatting.DARK_RED + "The command does not exist.");
    }
    
    private void add(Command... commands)
    {
        this.commands.addAll(Arrays.asList(commands));
    }
    
    public Command getCommand(String name)
    {
        for (Command command : commands)
        {
            if (command.name.equalsIgnoreCase(name) || command.shortForm.equalsIgnoreCase(name))
                return command;
        }
        
        return null;
    }
    
    public ArrayList<Command> get() { return commands; }
}
