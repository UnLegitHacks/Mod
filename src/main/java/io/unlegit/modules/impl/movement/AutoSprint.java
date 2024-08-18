package io.unlegit.modules.impl.movement;

import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;

@IModule(name = "Auto Sprint", description = "Automatically sprints for you.")
public class AutoSprint extends ModuleU
{
    public void onUpdate()
    {
        mc.options.keySprint.setDown(true);
    }
    
    public void onDisable() { super.onDisable(); mc.options.keySprint.setDown(false); }
}
