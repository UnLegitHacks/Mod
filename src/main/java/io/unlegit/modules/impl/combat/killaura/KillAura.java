package io.unlegit.modules.impl.combat.killaura;

import io.unlegit.UnLegit;
import io.unlegit.events.impl.entity.AttackE;
import io.unlegit.events.impl.entity.MotionE;
import io.unlegit.events.impl.entity.StrafeE;
import io.unlegit.gui.clickgui.ClickGui;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.impl.player.Cooldown;
import io.unlegit.modules.settings.impl.*;
import io.unlegit.utils.ElapTime;
import io.unlegit.utils.entity.*;
import io.unlegit.utils.network.Packets;
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

    public ToggleSetting swingHand = new ToggleSetting("Swing Hand", "Swings the hand normally.", true),
                         smartRange = new ToggleSetting("Smart Range", "Increases the range depending on your ping.", false),
                         predict = new ToggleSetting("Predict Pos", "Predicts the movement of the target.", true),
                         rayTrace = new ToggleSetting("Ray Trace", "Helps bypass anti-cheats.", true);
                         // targetESP = new ToggleSetting("Target ESP", true);
    
    /**
     * If the rotation mode is smooth, K.A. does not flag at all (along with
     * raytrace) especially while fighting multiple targets; however, hits are less
     * accurate, so smooth is not recommended for HvHs.
     */
    public ModeSetting rotations = new ModeSetting("Rotations", "The mode for rotations.", new String[]
    {
        "Vanilla", "Smooth", "Legit", "Snap", "None"
    }),
    priority = new ModeSetting("Priority", "The priority for the target.", new String[] {"Hurt Time", "Distance", "Health"}),
    autoBlock = new ModeSetting("Auto Block", "The mode for auto block.", new String[] {"None", "Vanilla", "Fake"});

    public ToggleSetting lockView = new ToggleSetting("Lock View", "Makes rotations client-side helping it bypass.", false),
                         strafeFix = new ToggleSetting("Move Fix", "If not Lock View, this also helps KillAura bypass.", false);

    private final ElapTime elapTime = new ElapTime();
    private boolean stopBlocking = false;
    public float CPS = 0, yaw, pitch;
    public LivingEntity target;
    
    public void onDisable()
    {
        super.onDisable();
        
        if (stopBlocking)
        {
            AutoBlock.unblock();
            stopBlocking = false;
        }
    }
    
    public void onUpdate()
    {
        float range = distance.value;
        
        if (smartRange.enabled && !mc.hasSingleplayerServer() && PlayerUtil.isInMotion())
        {
            PlayerInfo playerInfo = mc.getConnection().getPlayerInfo(mc.getUser().getName());
            // Null check for bedrock servers (which is coming soon with ViaBedrock)
            if (playerInfo != null) range += PlayerUtil.getSpeed() * (playerInfo.getLatency() / 175F);
        }
        
        target = TargetUtil.getTarget(mc.player, range + 1, priority.selected);
        
        if (target != null && !mc.player.isDeadOrDying())
        {
            if (mc.screen != null && !(mc.screen instanceof ChatScreen) && !(mc.screen instanceof PauseScreen) && !(mc.screen instanceof ClickGui)) mc.setScreen(null);
            Cooldown cooldown = (Cooldown) UnLegit.modules.get("Cooldown");
            
            if (autoBlock.equals("Vanilla"))
            {
                AutoBlock.block(target);
                if (!stopBlocking) stopBlocking = true;
            }
            
            mc.crosshairPickEntity = target;
            
            if (mc.player.distanceTo(target) <= range && (elapTime.passed((long) (1000 / CPS)) && !cooldown.isEnabled()) || (cooldown.isEnabled() && !cooldown.cancelHit()))
            {
                if (autoBlock.equals("Vanilla")) AutoBlock.unblock();
                if (rayTrace.enabled && !RotationUtil.rayTrace(target, yaw, pitch, range)) return;
                
                UnLegit.events.post(AttackE.get());
                mc.gameMode.attack(mc.player, target);
                swingItem();
                
                CPS = updateCPS();
            }
        }
        
        else if (stopBlocking)
        {
            AutoBlock.unblock(); stopBlocking = false;
        }
    }
    
    public void onMotion(MotionE e)
    {
        if (target != null)
        {
            e.yaw = yaw;
            e.pitch = pitch;
            e.changed = true;
        }
    }

    public void onStrafe(StrafeE e)
    {
        if (strafeFix.enabled && target != null && !rotations.equals("None"))
        {
            e.yaw = yaw;
            e.changed = true;
        }
    }

    public void onPlayerTurn()
    {
        updateRotations();

        if (lockView.enabled && target != null && !rotations.equals("None"))
        {
            mc.player.setYRot(yaw);
            mc.player.setXRot(pitch);
        }
    }
    
    public void onWorldChange() { toggle(); }
    
    public void swingItem()
    {
        if (swingHand.enabled) mc.player.swing(InteractionHand.MAIN_HAND);
        else Packets.send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
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
    
    // Uses Math.random() to be virtually undetectable
    private void legitenRotations(float[] rotations)
    {
        float yawDifference = Math.abs(yaw - rotations[0]), pitchDifference = Math.abs(pitch - rotations[1]);
        if (yawDifference > 90 || pitchDifference > 90) smoothenRotations(rotations);
        
        if (yaw < rotations[0])
        {
            yaw += (float) (48 * Math.random());
            if (rotations[0] < yaw) yaw = rotations[0];
        }
        
        else if (yaw > rotations[0])
        {
            yaw -= (float) (48 * Math.random());
            if (rotations[0] > yaw) yaw = rotations[0];
        }
        
        if (pitch < rotations[1])
        {
            pitch += (float) (48 * Math.random());
            if (rotations[1] < pitch) pitch = rotations[1];
        }
        
        else if (pitch > rotations[1])
        {
            pitch -= (float) (48 * Math.random());
            if (rotations[1] > pitch) pitch = rotations[1];
        }
    }

    public void updateRotations()
    {
        if (target != null)
        {
            float[] rotations = RotationUtil.rotations(target, predict.enabled);

            if (this.rotations.equals("Smooth")) smoothenRotations(rotations);

            else if (this.rotations.equals("Snap"))
            {
                float yawDifference = Math.abs(yaw - rotations[0]), pitchDifference = Math.abs(pitch - rotations[1]);
                if (yawDifference > 10) yaw = rotations[0];
                if (pitchDifference > 10) pitch = rotations[1];
            }

            else if (this.rotations.equals("Legit"))
                legitenRotations(rotations);

            else if (this.rotations.equals("None"))
            {
                yaw = mc.player.getYRot();
                pitch = mc.player.getXRot();
            }

            else { yaw = rotations[0]; pitch = rotations[1]; }
        }

        else if (rotations.equals("Smooth"))
        {
            yaw = mc.player.getYRot(); pitch = mc.player.getXRot();
        }
    }
    
    private float updateCPS()
    {
        float min = minCPS.value, max = maxCPS.value;
        return (float) (min + (max - min) * Math.random());
    }
}
