package io.unlegit.modules.impl.combat.killaura;

import io.unlegit.UnLegit;
import io.unlegit.events.impl.entity.AttackE;
import io.unlegit.events.impl.entity.MotionE;
import io.unlegit.gui.clickgui.ClickGui;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.impl.combat.Criticals;
import io.unlegit.modules.impl.player.Cooldown;
import io.unlegit.modules.settings.impl.ModeSetting;
import io.unlegit.modules.settings.impl.SliderSetting;
import io.unlegit.modules.settings.impl.ToggleSetting;
import io.unlegit.utils.ElapTime;
import io.unlegit.utils.entity.PlayerUtil;
import io.unlegit.utils.entity.RotationUtil;
import io.unlegit.utils.entity.TargetUtil;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;

@IModule(name = "Kill Aura", description = "Automatically attacks entities for you.")
public class KillAura extends ModuleU
{
    public SliderSetting minCPS = new SliderSetting("Min CPS", "The minimum CPS in randomization.", 1, 8, 20),
                         maxCPS = new SliderSetting("Max CPS", "The maximum CPS in randomization.", 1, 12, 20),
                         distance = new SliderSetting("Distance", "The distance the target should be in.", 3, 3, 5);
    
    public ToggleSetting swing = new ToggleSetting("Swing", "Swings the held item client-side.", true),
                         smartRange = new ToggleSetting("Smart Range", "Increases the range depending on your ping.", false),
                         teams = new ToggleSetting("Teams", "Don't attack players on your team.", false),
                         predict = new ToggleSetting("Predict Pos", "Predicts the movement of the target.", true);
                         // targetESP = new ToggleSetting("Target ESP", true);
    
    /**
     * If the rotation mode is smooth, K.A. does not flag at all (along with
     * raytrace) especially while fighting multiple targets; however, hits are less
     * accurate, so smooth is not recommended for HvHs.
     */
    public ModeSetting rotations = new ModeSetting("Rotations", "The mode for rotations.", new String[] {"Vanilla", "Smooth"}),
                       priority = new ModeSetting("Priority", "The priority for the target.", new String[] {"Hurt Time", "Distance", "Health"}),
                       autoBlock = new ModeSetting("Auto Block", "The mode for auto block.", new String[] {"None", "Vanilla", "Fake"});
    
    private ElapTime elapTime = new ElapTime();
    private boolean stopBlocking = false;
    public float CPS = 0, yaw, pitch;
    public LivingEntity target;
    
    public void onUpdate()
    {
        float range = distance.value;
        
        if (smartRange.enabled && !mc.hasSingleplayerServer() && PlayerUtil.isInMotion())
        {
            PlayerInfo playerInfo = mc.getConnection().getPlayerInfo(mc.getUser().getName());
            // Null check for bedrock servers (which is coming soon with ViaBedrock)
            if (playerInfo != null) range += PlayerUtil.getSpeed(
                    mc.player.getDeltaMovement()) * (playerInfo.getLatency() / 200F);
        }
        
        target = TargetUtil.getTarget(mc.player, range, priority.mode);
        
        if (target != null)
        {
            if (mc.screen != null && !(mc.screen instanceof ChatScreen) && !(mc.screen instanceof PauseScreen) && !(mc.screen instanceof ClickGui)) mc.setScreen(null);
            Criticals criticals = (Criticals) UnLegit.modules.get("Criticals");
            Cooldown cooldown = (Cooldown) UnLegit.modules.get("Cooldown");
            
            if (autoBlock.equals("Vanilla"))
            {
                AutoBlock.block();
                if (!stopBlocking) stopBlocking = true;
            }
            
            if ((elapTime.passed((long) (1000 / CPS)) && !cooldown.isEnabled()) || (cooldown.isEnabled() && !cooldown.cancelHit()))
            {
                if (autoBlock.equals("Vanilla")) AutoBlock.unblock();
                
                if (!RotationUtil.rayTrace(target, yaw, pitch, range)) return;
                
                mc.gameMode.attack(mc.player, target);
                swingItem();
                
                if (criticals.isEnabled()) criticals.onAttack(AttackE.get());
                
                CPS = updateCPS();
            }
        } else if (stopBlocking)
        {
            AutoBlock.unblock(); stopBlocking = false;
        }
    }
    
    public void onMotion(MotionE e)
    {
        if (target != null)
        {
            float[] rotations = RotationUtil.rotations(target);
            
            if (this.rotations.equals("Smooth")) smoothenRotations(rotations);
            else yaw = rotations[0]; pitch = rotations[1];
            
            e.yaw = yaw; e.pitch = pitch;
        } else if (rotations.equals("Smooth"))
        {
            yaw = mc.player.getYRot(); pitch = mc.player.getXRot();
        }
    }
    
    public void swingItem()
    {
        if (swing.enabled) mc.player.swing(InteractionHand.MAIN_HAND);
        else mc.getConnection().send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
    }
    
    // Smooth rotations
    private void smoothenRotations(float[] rotations)
    {
        float yawDifference = Math.abs(rotations[0] - yaw), pitchDifference = Math.abs(rotations[1] - pitch);
        
        if (yaw < rotations[0])
        {
            if (yawDifference < 5) yaw = rotations[0];
            else yaw += (rotations[0] - yaw) / 2;
        }
        
        else if (yaw > rotations[0])
        {
            if (yawDifference < 5) yaw = rotations[0];
            else yaw -= (yaw - rotations[0]) / 2;
        }
        
        if (pitch < rotations[1])
        {
            if (pitchDifference < 5) pitch = rotations[1];
            else pitch += (rotations[1] - pitch) / 2;
        }
        
        else if (pitch > rotations[1])
        {
            if (pitchDifference < 5) pitch = rotations[1];
            else pitch -= (pitch - rotations[1]) / 2;
        }
    }
    
    private float updateCPS()
    {
        float min = minCPS.value, max = maxCPS.value;
        return (float) (min + (max - min) * Math.random());
    }
}
