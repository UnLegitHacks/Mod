package io.unlegit.modules.impl.misc;

import io.unlegit.UnLegit;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;

@IModule(name = "Teams", description = "Makes modules exclude members on your team.")
public class Teams extends ModuleU
{
    public static boolean active()
    {
        return UnLegit.modules.get("Teams").isEnabled();
    }
}
