package io.unlegit.modules.impl.combat.killaura;

import io.unlegit.UnLegit;
import io.unlegit.events.impl.AttackE;
import io.unlegit.events.impl.MotionE;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.impl.combat.Criticals;
import io.unlegit.modules.impl.player.Cooldown;
import io.unlegit.modules.settings.impl.ModeSetting;
import io.unlegit.modules.settings.impl.SliderSetting;
import io.unlegit.modules.settings.impl.ToggleSetting;
import io.unlegit.utils.ElapTime;
import io.unlegit.utils.RotationUtil;
import io.unlegit.utils.TargetUtil;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;

@IModule(name = "Kill Aura", description = "Automatically attacks entities for you.")
public class KillAura extends ModuleU
{
    public SliderSetting minCPS = new SliderSetting("Min CPS", 1, 8, 20),
                         maxCPS = new SliderSetting("Max CPS", 1, 12, 20),
                         distance = new SliderSetting("Distance", 3, 3, 5);
    
    public ToggleSetting swing = new ToggleSetting("Swing", true),
                         // Will increase your range depending on your ping.
                         smartRange = new ToggleSetting("Smart Range", false),
                         teams = new ToggleSetting("Teams", false);
                         // targetESP = new ToggleSetting("Target ESP", true);
    
    /**
     * If the rotation mode is smooth, K.A. does not flag at all (along with
     * raytrace) especially while fighting multiple targets; however, hits are less
     * accurate, so smooth is not recommended for HvHs.
     */
    public ModeSetting rotationsMode = new ModeSetting("Rotations Mode", new String[] {"Vanilla", "Smooth"}),
                       priority = new ModeSetting("Priority", new String[] {"Hurt Time", "Distance", "Health"}),
                       autoBlock = new ModeSetting("Auto Block", new String[] {"None", "Vanilla", "Fake"});
    
    private ElapTime elapTime = new ElapTime();
    public float CPS = 0, yaw, pitch;
    public LivingEntity target;
    
    public void onUpdate()
    {
        float range = distance.currentValue;
        
        if (smartRange.enabled && !mc.hasSingleplayerServer())
        {
            PlayerInfo playerInfo = mc.getConnection().getPlayerInfo(mc.getUser().getName());
            if (playerInfo != null) range += playerInfo.getLatency() / 1000F;
            // Null check for Bedrock servers
        }
        
        target = TargetUtil.getTarget(mc.player, range, priority.currentMode);
        
        if (target != null)
        {
            Cooldown cooldown = (Cooldown) UnLegit.modules.get("Cooldown");
            
            if (elapTime.passed((long) (1000 / CPS)) && !cooldown.isEnabled() || (cooldown.isEnabled() && !cooldown.cancelHit()))
            {
                Criticals criticals = (Criticals) UnLegit.modules.get("Criticals");
                mc.hitResult = new EntityHitResult(target);
                if (autoBlock.equals("Vanilla")) AutoBlock.unblock();
                mc.gameMode.attack(mc.player, target);
                swingItem();
                criticals.onAttack(AttackE.get());
                CPS = updateCPS();
                if (autoBlock.equals("Vanilla")) AutoBlock.block();
            }
        }
    }
    
    public void onMotion(MotionE e)
    {
        if (target != null)
        {
            float[] rotations = RotationUtil.rotations(target);
            
            if (rotationsMode.equals("Smooth"))
            {
                /**
                 * Smooth rotations
                 */
                if (yaw < rotations[0]) yaw += (rotations[0] - yaw) / 1.5F;
                else if (yaw > rotations[0]) yaw -= (yaw - rotations[0]) / 1.5F;
                if (pitch < rotations[1]) pitch += (rotations[1] - pitch) / 1.5F;
                else if (pitch > rotations[1]) pitch -= (pitch - rotations[1]) / 1.5F;
            }
            
            else
            {
                yaw = rotations[0];
                pitch = rotations[1];
            }
            
            e.yaw = yaw; e.pitch = pitch; mc.player.yHeadRot = yaw; mc.player.yBodyRot = yaw;
        }
        
        else if (yaw != mc.player.getYRot() || pitch != mc.player.getXRot())
            yaw = mc.player.getYRot(); pitch = mc.player.getXRot();
    }
    
    public void swingItem()
    {
        if (swing.enabled) mc.player.swing(InteractionHand.MAIN_HAND);
        else mc.getConnection().send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
    }
    
    private float updateCPS()
    {
        float min = minCPS.currentValue, max = maxCPS.currentValue;
        return (float) (min + (max - min) * Math.random());
    }
}
