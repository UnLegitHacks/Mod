package io.unlegit.bots.impl;

import io.unlegit.UnLegit;
import io.unlegit.bots.Bot;
import io.unlegit.bots.StrafeEntity;
import io.unlegit.events.impl.client.MessageE;
import io.unlegit.events.impl.entity.AttackE;
import io.unlegit.events.impl.entity.MotionE;
import io.unlegit.events.impl.render.GuiRenderE;
import io.unlegit.modules.impl.player.Cooldown;
import io.unlegit.utils.ElapTime;
import io.unlegit.utils.entity.RotationUtil;
import io.unlegit.utils.entity.TargetUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;

public class SumoBot extends Bot
{
    private final ElapTime elapTime = new ElapTime();
    public boolean gameStarted = true, down = false;
    public LivingEntity target;
    public float yaw, pitch;

    public void onUpdate()
    {
        target = gameStarted ? TargetUtil.getTarget(mc.player, 128, "Distance") : null;
        float reach = mc.getCurrentServer().ip.endsWith(":19132") /* Bedrock */ ? 4 : 3;

        if (target != null)
        {
            mc.options.keyUp.setDown(down = true);
            Cooldown cooldown = (Cooldown) UnLegit.modules.get("Cooldown");
            mc.crosshairPickEntity = target;

            if (mc.player.distanceTo(target) <= reach)
            {
                StrafeEntity.tick();

                if ((elapTime.passed((long) (1000 / CPS)) && !cooldown.isEnabled()) || (cooldown.isEnabled() && !cooldown.cancelHit()))
                {
                    if (!RotationUtil.rayTrace(target, yaw, pitch, reach) && reach != 4) return;
                    UnLegit.events.post(AttackE.get());

                    mc.gameMode.attack(mc.player, target);
                    mc.player.swing(InteractionHand.MAIN_HAND);

                    CPS = updateCPS();
                }
            }

            else if (mc.player.distanceTo(target) <= (reach + 2))
                StrafeEntity.tick();
            else
                StrafeEntity.stop();
        }

        else if (down)
        {
            mc.options.keyUp.setDown(down = false);
            StrafeEntity.stop();
        }
    }

    public void onGuiRender(GuiRenderE e)
    {
        if (target != null)
        {
            mc.player.setYRot(yaw);
            mc.player.setXRot(pitch);
        }
    }

    public void onMotion(MotionE e)
    {
        if (target != null)
        {
            float[] rotations = RotationUtil.rotations(target);
            yaw = rotations[0]; pitch = rotations[1];
        }
    }

    public void onMessageReceive(MessageE e)
    {
        String message = ChatFormatting.stripFormatting(e.message.getString().replaceAll("\r", "\\\\r").replaceAll("\n", "\\\\n"));

        if (message.contains("Eliminate"))
            gameStarted = true;

        else if (message.contains(" by "))
            gameStarted = false;
    }

    public void onDisable()
    {
        super.onDisable();
        gameStarted = false;
    }
}
