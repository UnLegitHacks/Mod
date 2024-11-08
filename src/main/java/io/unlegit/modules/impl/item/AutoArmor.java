package io.unlegit.modules.impl.item;

import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.impl.SliderSetting;
import io.unlegit.modules.settings.impl.ToggleSetting;

@IModule(name = "Auto Armor", description = "Automatically equips armor for you.")
public class AutoArmor extends ModuleU
{
    public SliderSetting minDelay = new SliderSetting("Min Delay (ms)", "The minimum delay in randomization.", 0, 50, 1000),
                         maxDelay = new SliderSetting("Max Delay (ms)", "The maximum delay in randomization.", 0, 100, 1000);

    public ToggleSetting invOpen = new ToggleSetting("Inv Open Only", "Only equips armor when the inventory is open", true);

}
