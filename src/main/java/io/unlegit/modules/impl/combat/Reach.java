package io.unlegit.modules.impl.combat;

import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.impl.SliderSetting;

@IModule(name = "Reach", description = "Allows you to interact with entities from a greater range.")
public class Reach extends ModuleU
{
    public SliderSetting combatReach = new SliderSetting("Combat Reach", "The reach for combat.", 3, 3, 6);
}
