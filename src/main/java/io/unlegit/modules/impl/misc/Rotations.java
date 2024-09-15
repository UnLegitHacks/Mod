package io.unlegit.modules.impl.misc;

import io.unlegit.events.impl.entity.MotionE;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;

@IModule(name = "Rotations", description = "Shows rotations changed by modules.")
public class Rotations extends ModuleU
{
    public float prevPitch = 0;
    
    public void onUpdate()
    {
        prevPitch = MotionE.get().pitch;
    }
    
    public void onPostMotion(MotionE e)
    {
        if (e.changed)
            mc.player.yHeadRot = mc.player.yBodyRot = e.yaw;
    }
}
