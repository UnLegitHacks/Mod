package io.unlegit.commands.impl;

import io.unlegit.UnLegit;
import io.unlegit.commands.Command;
import io.unlegit.config.UnConfig;
import io.unlegit.events.EventListener;
import io.unlegit.interfaces.ICommand;
import io.unlegit.modules.ModuleU;
import io.unlegit.utils.render.ScreenUtil;
import net.minecraft.ChatFormatting;

@ICommand(name = "Bind", shortForm = "B", usage = ".bind <module> <key>")
public class BindCommand extends Command implements EventListener
{
    public void onExecute(String[] args)
    {
        if (!validateArgs(args, 2)) return;
        ModuleU module = UnLegit.modules.get(args[0]);
        
        if (module != null)
        {
            int key = args[1].equalsIgnoreCase("NONE") ? 0 : ScreenUtil.getKey(args[1]);
            
            if (key == -1)
            {
                clientMessage(ChatFormatting.DARK_RED +
                        "Bad key!");
                return;
            }
            
            module.key = key;
            clientMessage(ChatFormatting.GREEN +
                    "Bound " + module.name + " to key " + args[1].toUpperCase());
            UnConfig.save();
        }
        
        else clientMessage(ChatFormatting.DARK_RED +
                "Module " + args[0] + " not found!");
    }
}
