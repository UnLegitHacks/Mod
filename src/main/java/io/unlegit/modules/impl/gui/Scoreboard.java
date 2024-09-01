package io.unlegit.modules.impl.gui;

import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.impl.ToggleSetting;

@IModule(name = "Scoreboard", description = "Customize the scoreboard!")
public class Scoreboard extends ModuleU
{
    public ToggleSetting makeSpaceForModules = new ToggleSetting("Make Space for Modules", "When Active Mods is enabled, moves down accordingly.", true);
    
    public Scoreboard() { toggle(); }
}
