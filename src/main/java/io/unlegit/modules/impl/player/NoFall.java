package io.unlegit.modules.impl.player;

import io.unlegit.events.impl.entity.MotionE;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.impl.ModeSetting;

@IModule(name = "No Fall", description = "Does not let you take fall damage.")
public class NoFall extends ModuleU
{
    public ModeSetting mode = new ModeSetting("Mode", "The mode for No Fall.", new String[]
    {
        /**
         * Legit makes you take a bit of fall damage so that
         * you do not appear suspicious to staff and others,
         * though it does not bypass.
         */
        "Vanilla", "Legit"
    });
    
    public void onMotion(MotionE e)
    {
        if (mc.player.fallDistance >= (mode.equals("Vanilla") ? 3 : 4))
        {
            e.onGround = true;
            mc.player.fallDistance = 0;
        }
    }
}
