package io.unlegit.modules.impl.player;

import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;

@IModule(name = "Auto Respawn", description = "Automatically respawns the player.")
public class AutoRespawn extends ModuleU
{
    public void onUpdate()
    {
        if (mc.player.isDeadOrDying()) mc.player.respawn();
    }
}
