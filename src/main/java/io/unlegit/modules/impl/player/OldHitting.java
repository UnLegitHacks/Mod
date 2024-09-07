package io.unlegit.modules.impl.player;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import io.unlegit.UnLegit;
import io.unlegit.interfaces.IModule;
import io.unlegit.mixins.item.InvItemRender;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.impl.combat.killaura.KillAura;
import io.unlegit.modules.settings.impl.ModeSetting;
import io.unlegit.modules.settings.impl.SliderSetting;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.SwordItem;

@IModule(name = "Old Hitting", description = "Lets you block your sword.")
public class OldHitting extends ModuleU
{
    public ModeSetting blockHit = new ModeSetting("Block Hit Mode", "The animation for block hitting.", new String[]
    {
        "Vanilla", "Upward", "Leaked", "Scale", "None"
    });
    
    public SliderSetting scale = new SliderSetting("Scale", "The size of the sword.", 0.5F, 1, 2),
                         posX = new SliderSetting("Position X", "The position X of the sword.", -1, 0, 1),
                         posY = new SliderSetting("Position Y", "The position Y of the sword.", -1, 0, 1),
                         posZ = new SliderSetting("Position Z", "The position Z of the sword.", -1, 0, 1);
    
    public void performAnimation(PoseStack pose, float swingProgress, float equipProgress)
    {
        undoEquipChange(pose, equipProgress);
        
        // Translation
        pose.translate(-0.1F + (posX.value / 4), 0.1F + (posY.value / 4), 0.1F + (posZ.value / 4));
        pose.scale(scale.value, scale.value, scale.value);
        
        if (blockHit.equals("Vanilla"))
            ((InvItemRender) mc.gameRenderer.itemInHandRenderer).invokeApplyItemArmAttackTransform(pose, HumanoidArm.RIGHT, swingProgress);
        else if (!blockHit.equals("None"))
            blockHit(blockHit.selected, pose, swingProgress);
        
        // Block position.
        pose.mulPose(Axis.XP.rotationDegrees(-102.25F));
        pose.mulPose(Axis.YP.rotationDegrees(13.365F));
        pose.mulPose(Axis.ZP.rotationDegrees(78.05F));
    }
    
    public void blockHit(String mode, PoseStack pose, float swingProgress)
    {
        float x = Mth.sin(Mth.sqrt(swingProgress) * (float) Math.PI),
              y = Mth.sin(swingProgress * swingProgress * (float) Math.PI);
        
        switch (mode)
        {
            case "Upward":
                pose.mulPose(Axis.ZP.rotationDegrees(x * -20));
                break;
            case "Leaked":
                pose.mulPose(Axis.YP.rotationDegrees(y * 30));
                pose.mulPose(Axis.ZP.rotationDegrees(x * 30));
                pose.mulPose(Axis.XP.rotationDegrees(x * 30));
                break;
            case "Scale":
                pose.scale(1 - (y / 4), 1 - (y / 4), 1 - (y / 4));
                break;
        }
    }
    
    // Makes the sword not go down to actually see the block-hit animation.
    public void undoEquipChange(PoseStack pose, float equipProgress)
    {
        pose.translate(0, equipProgress * 0.6F, 0);
    }
    
    public boolean playerBlocking()
    {
        KillAura killAura = (KillAura) UnLegit.modules.get("Kill Aura");
        return (mc.player.getMainHandItem().getItem() instanceof SwordItem && mc.options.keyUse.isDown())
                || (killAura.isEnabled() && killAura.target != null && !killAura.autoBlock.equals("None"));
    }
}
