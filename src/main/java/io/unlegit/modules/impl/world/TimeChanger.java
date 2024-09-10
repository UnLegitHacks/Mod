package io.unlegit.modules.impl.world;

import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.impl.SliderSetting;

@IModule(name = "Time Changer", description = "Changes the time in the world visually.")
public class TimeChanger extends ModuleU
{
    public SliderSetting time = new SliderSetting("Time", "1000 = Day, 6000 = Noon, 13000 = Night, & 18000 = Midnight.", 0, 1000, 24000);
}
