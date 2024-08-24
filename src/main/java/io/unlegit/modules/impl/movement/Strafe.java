package io.unlegit.modules.impl.movement;

import io.unlegit.events.impl.entity.MoveE;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.utils.entity.PlayerUtil;

@IModule(name = "Strafe", description = "Allows you to move more freely.")
public class Strafe extends ModuleU
{
    public void onMove(MoveE e)
    {
        if (PlayerUtil.isMoving()) e.vec3 = PlayerUtil.strafe(e.vec3);
    }
}
