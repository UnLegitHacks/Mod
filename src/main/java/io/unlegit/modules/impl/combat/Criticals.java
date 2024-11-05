package io.unlegit.modules.impl.combat;

import io.unlegit.events.impl.entity.AttackE;
import io.unlegit.events.impl.entity.MotionE;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.impl.ModeSetting;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

@IModule(name = "Criticals", description = "Always does critical hits when you attack someone.")
public class Criticals extends ModuleU
{
    public ModeSetting mode = new ModeSetting("Mode", "The mode for criticals.", new String[]
    {
        "Jump", "Hop", "Packet"
    });
    
    private final double[] packetJumps = new double[] { 0.0625D, 0 };
    private int tick = packetJumps.length;
    private boolean jumped = false;
    
    public void onAttack(AttackE e)
    {
        if (mc.player.onGround() && !mc.options.keyJump.isDown() && mc.crosshairPickEntity != null)
        {
            if (mode.equals("Jump") || mode.equals("Hop"))
            {
                mc.player.jumpFromGround();
                jumped = true;
            } else if (tick == packetJumps.length) tick = 0;
        }
    }
    
    public void onMotion(MotionE e)
    {
        if (mode.equals("Packet") && mc.player.onGround() && tick != packetJumps.length)
        {
            e.y += packetJumps[tick];
            e.onGround = false;
            tick++;
        }
        
        else if (mode.equals("Hop") && jumped)
        {
            mc.player.addDeltaMovement(new Vec3(0, -LivingEntity.BASE_JUMP_POWER / 2, 0));
            jumped = false;
        }
    }
}
