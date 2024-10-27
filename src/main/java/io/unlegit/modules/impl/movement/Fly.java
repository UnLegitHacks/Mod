package io.unlegit.modules.impl.movement;

import io.unlegit.events.impl.block.BlockShapeE;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.impl.ModeSetting;
import io.unlegit.modules.settings.impl.SliderSetting;
import io.unlegit.utils.entity.PlayerUtil;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;

@IModule(name = "Fly", description = "Allows you to fly in survival mode")
public class Fly extends ModuleU
{
    public SliderSetting speed = new SliderSetting("Speed", "The speed for flying.", 0, 1, 10);
    public ModeSetting mode = new ModeSetting("Mode", "The mode for flying.", new String[] {"Vanilla", "Airwalk"});
    
    public void onUpdate()
    {
        if (mode.equals("Vanilla"))
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
    
    public void onBlockShape(BlockShapeE e)
    {
        if (mode.equals("Airwalk") && !(e.state.getBlock() instanceof LiquidBlock) &&
                mc.player != null && e.pos.getY() < mc.player.getY())
        {
            e.shape = Shapes.block();
            e.changed = true;
        }
    }
}
