package io.unlegit.modules.impl.player;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import io.unlegit.UnLegit;
import io.unlegit.interfaces.IModule;
import io.unlegit.mixins.item.InvItemRenderer;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.impl.combat.killaura.KillAura;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.SwordItem;

@IModule(name = "Old Hitting", description = "Allows you to sword block.")
public class OldHitting extends ModuleU
{
    public void performAnimation(PoseStack poseStack, float swingProgress, float equipProgress)
    {
        undoEquipChange(poseStack, equipProgress);
        
        // Translation
        poseStack.translate(-0.1F, 0.1F, 0.1F);
        ((InvItemRenderer) mc.gameRenderer.itemInHandRenderer).invokeApplyItemArmAttackTransform(poseStack, HumanoidArm.RIGHT, swingProgress);
        
        // Block position.
        poseStack.mulPose(Axis.XP.rotationDegrees(-102.25F));
        poseStack.mulPose(Axis.YP.rotationDegrees(13.365F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(78.05F));
    }
    
    // Makes the sword not go down to actually see the block-hit animation.
    public void undoEquipChange(PoseStack poseStack, float equipProgress)
    {
        poseStack.translate(0, equipProgress * 0.6F, 0);
    }
    
    public boolean playerBlocking()
    {
        KillAura killAura = (KillAura) UnLegit.modules.get("Kill Aura");
        return (mc.player.getMainHandItem().getItem() instanceof SwordItem && mc.options.keyUse.isDown())
                || (killAura.isEnabled() && killAura.target != null && !killAura.autoBlock.equals("None"));
    }
}