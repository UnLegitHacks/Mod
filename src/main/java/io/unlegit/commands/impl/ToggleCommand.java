package io.unlegit.commands.impl;

import io.unlegit.UnLegit;
import io.unlegit.commands.Command;
import io.unlegit.config.UnConfig;
import io.unlegit.interfaces.ICommand;
import io.unlegit.modules.ModuleU;
import net.minecraft.ChatFormatting;

@ICommand(name = "Toggle", shortForm = "T", usage = ".toggle <module>")
public class ToggleCommand extends Command
{
    public void onExecute(String[] args)
    {
        if (!validateArgs(args, 1)) return;
        ModuleU module = UnLegit.modules.get(args[0]);
        
        if (module != null)
        {
            module.toggle();
            UnConfig.save();
            
            if (module.isEnabled())
                clientMessage(ChatFormatting.GREEN +
                        "Enabled " + module.name + ".");
            else clientMessage(ChatFormatting.RED +
                    "Disabled " + module.name + ".");
        }
        
        else clientMessage(ChatFormatting.DARK_RED +
                "Module " + args[0] + " not found!");
    }
}
