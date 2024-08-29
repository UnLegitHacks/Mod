package io.unlegit.modules.impl.combat.killaura;

import io.unlegit.interfaces.IMinecraft;
import net.minecraft.world.entity.Entity;

public class AutoBlock implements IMinecraft
{
    public static Entity blockingTarget = null;
    
    public static void block(Entity target)
    {
        mc.options.keyUse.setDown(true);
        blockingTarget = target;
    }
    
    public static void unblock()
    {
        mc.options.keyUse.setDown(false);
        blockingTarget = null;
    }
}
