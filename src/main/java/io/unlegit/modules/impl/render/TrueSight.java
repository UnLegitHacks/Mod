package io.unlegit.modules.impl.render;

import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.impl.ToggleSetting;

@IModule(name = "True Sight", description = "Lets you see invisible entities and barriers.")
public class TrueSight extends ModuleU
{
    public ToggleSetting invisibleEntities = new ToggleSetting("Invisible Entities", "Lets you see invisible entities.", true),
                         barriers = new ToggleSetting("Barriers", "Lets you see barriers.", false);
}
