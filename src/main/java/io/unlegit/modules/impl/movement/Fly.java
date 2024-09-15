package io.unlegit.modules.impl.movement;

import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.impl.SliderSetting;
import io.unlegit.utils.entity.PlayerUtil;
import net.minecraft.world.phys.Vec3;

@IModule(name = "Fly", description = "Allows you to fly in survival mode")
public class Fly extends ModuleU
{
    public SliderSetting speed = new SliderSetting("Speed", "The speed for flying.", 0, 1, 10);
    
    public void onUpdate()
    {
        float speed = this.speed.value,
              y = mc.options.keyJump.isDown() ? speed :
                  mc.options.keyShift.isDown() ? -speed : 0;
        
        if (!PlayerUtil.isMoving())
            mc.player.setDeltaMovement(0, y, 0);
        
        else
        {
            Vec3 deltaMovement = PlayerUtil.strafe(speed);
            mc.player.setDeltaMovement(new Vec3(deltaMovement.x, y, deltaMovement.z));
        }
    }
}
