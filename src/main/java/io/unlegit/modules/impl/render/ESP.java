package io.unlegit.modules.impl.render;

import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.impl.ModeSetting;

@IModule(name = "ESP", description = "Allows you to see entities through blocks.")
public class ESP extends ModuleU
{
    public ModeSetting mode = new ModeSetting("Mode", "The mode for ESP.", new String[]
    {
        "Chams", "Outline"
    });
}
