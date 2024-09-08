package io.unlegit.modules.impl.movement;

import io.unlegit.events.impl.entity.MoveE;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.impl.ModeSetting;
import io.unlegit.modules.settings.impl.SliderSetting;
import io.unlegit.utils.entity.PlayerUtil;
import net.minecraft.world.phys.Vec3;

@IModule(name = "Speed", description = "Vroom vroom!")
public class Speed extends ModuleU
{
    public ModeSetting mode = new ModeSetting("Mode", "The mode for speed.", new String[] 
    {
        "Vanilla", "Strafe"
    });
    
    public SliderSetting speed = new SliderSetting("Speed", "How fast you should go.", 1, 1, 2);
    
    public Speed()
    {
        mode.setAction(() -> speed.hidden = !mode.equals("Vanilla"));
    }
    
    /** Strafe lets you move without friction. */
    public void onUpdate()
    {
        if (mode.equals("Strafe") && PlayerUtil.isMoving())
            mc.player.setDeltaMovement(PlayerUtil.strafe());
    }
    
    public void onMove(MoveE e)
    {
        if (mode.equals("Vanilla"))
        {
            float speed = this.speed.value;
            e.vec3 = new Vec3(e.vec3.x * speed, e.vec3.y, e.vec3.z * speed);
        }
    }
}
