package io.unlegit.commands;

import java.util.ArrayList;
import java.util.Arrays;

import io.unlegit.UnLegit;
import io.unlegit.commands.impl.BindCommand;
import io.unlegit.commands.impl.ToggleCommand;
import io.unlegit.interfaces.IMinecraft;
import net.minecraft.ChatFormatting;

public class CommandManager implements IMinecraft
{
    public ArrayList<Command> commands = new ArrayList<>();
    
    public CommandManager()
    {
        add(new ToggleCommand(), new BindCommand());
    }
    
    public void handle(String message)
    {
        String[] segments = message.contains(" ") ? message.split(" ") : new String[] {message};
        String name = segments[0].substring(1);
        Command command = getCommand(name);
        
        if (command != null)
            command.onExecute(segments.length == 1 ? null : Arrays.copyOfRange(segments, 1, segments.length));
        else clientMessage(UnLegit.PREFIX + ChatFormatting.DARK_RED + "The command does not exist.");
    }
    
    private void add(Command... commands)
    {
        for (Command command : commands) this.commands.add(command);
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
}
