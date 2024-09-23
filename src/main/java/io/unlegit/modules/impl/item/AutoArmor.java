package io.unlegit.modules.impl.item;

import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.impl.ToggleSetting;

@IModule(name = "Auto Armor", description = "Automatically equips armor for you.")
public class AutoArmor extends ModuleU
{
    public ToggleSetting invOpen = new ToggleSetting("Inv Open Only", "Only equips armor when the inventory is open", true);
}
