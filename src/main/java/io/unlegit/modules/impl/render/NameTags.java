package io.unlegit.modules.impl.render;

import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.impl.ToggleSetting;

@IModule(name = "Name Tags", description = "Allows you to customize name tags.")
public class NameTags extends ModuleU
{
    public ToggleSetting scale = new ToggleSetting("Scale", "Automatically magnifies name tags.", false),
                         infiniteRange = new ToggleSetting("Infinite Range", "Disables the 64 block limit.", false);
}
