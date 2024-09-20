package io.unlegit.commands.impl;

import io.unlegit.UnLegit;
import io.unlegit.commands.Command;
import io.unlegit.config.UnConfig;
import io.unlegit.interfaces.ICommand;
import io.unlegit.modules.ModuleU;
import net.minecraft.ChatFormatting;

/** Hides modules from ActiveMods. */
@ICommand(name = "Hide", shortForm = "h", exampleUse = ".hide <module>")
public class HideCommand extends Command
{
    public void onExecute(String[] args)
    {
        if (!validateArgs(args, 1)) return;
        ModuleU module = UnLegit.modules.get(args[0]);
        
        if (module != null)
        {
            module.hidden = !module.hidden;
            UnConfig.save();
            
            if (module.hidden)
                clientMessage(UnLegit.PREFIX + ChatFormatting.GREEN + "Hidden " + module.name + ".");
            else clientMessage(UnLegit.PREFIX + ChatFormatting.RED + "Unhidden " + module.name + ".");
        }
        
        else clientMessage(UnLegit.PREFIX + ChatFormatting.DARK_RED + "Module " + args[0] + " not found!");
    }
}
