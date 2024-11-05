package io.unlegit.modules.impl.combat;

import io.unlegit.events.impl.entity.AttackE;
import io.unlegit.events.impl.entity.MotionE;
import io.unlegit.interfaces.IModule;
import io.unlegit.mixins.client.AccKeyMap;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.impl.ModeSetting;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import static org.lwjgl.glfw.GLFW.glfwGetKey;

@IModule(name = "W-Tap", description = "Deals more knockback to entities.")
public class WTap extends ModuleU
{
    public ModeSetting mode = new ModeSetting("Mode", "The mode for W-Tap.", new String[]
    {
        "Normal", "Packet"
    });
    
    public Entity entity = null;
    public int ticks = 0;
    
    public void onAttack(AttackE e)
    {
        if (mc.crosshairPickEntity instanceof LivingEntity)
        {
            entity = mc.crosshairPickEntity;
            
            if (mode.equals("Normal") && ((LivingEntity) entity).hurtTime >= 6)
            {
                mc.options.keyUp.setDown(false);
                ticks = 0;
            }
        }
    }
    
    public void onUpdate()
    {
        if (mode.equals("Normal"))
        {
            if (ticks == 1)
            {
                ticks++;
                
                mc.options.keyUp.setDown(
                    glfwGetKey(mc.getWindow().getWindow(), ((AccKeyMap) mc.options.keyUp).getKey()
                        .getValue()) == 1);
            } else if (ticks != 2) ticks++;
        }
    }
    
    public void onMotion(MotionE e)
    {
        if (mode.equals("Packet") && entity != null && ((LivingEntity) entity).hurtTime > 9)
            e.sprinting = false;
    }
}
