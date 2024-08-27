package io.unlegit.modules.impl.combat.killaura;

import io.unlegit.interfaces.IMinecraft;

public class AutoBlock implements IMinecraft
{
    public static void block()
    {
        mc.options.keyUse.setDown(true);
    }
    
    public static void unblock()
    {
        mc.options.keyUse.setDown(false);
    }
}
