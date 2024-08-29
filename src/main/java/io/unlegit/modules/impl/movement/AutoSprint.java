package io.unlegit.modules.impl.movement;

import io.unlegit.UnLegit;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.impl.movement.scaffold.Scaffold;

@IModule(name = "Auto Sprint", description = "Automatically sprints for you.")
public class AutoSprint extends ModuleU
{
    public void onUpdate()
    {
        Scaffold scaffold = (Scaffold) UnLegit.modules.get("Scaffold");
        
        if (!(scaffold.isEnabled() && scaffold.sprint.equals("None")))
            mc.options.keySprint.setDown(true);
    }
    
    public void onDisable() { super.onDisable(); mc.options.keySprint.setDown(false); }
}
