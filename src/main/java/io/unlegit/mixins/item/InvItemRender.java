package io.unlegit.mixins.item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.entity.HumanoidArm;

@Mixin(ItemInHandRenderer.class)
public interface InvItemRender
{
    @Invoker("applyItemArmAttackTransform")
    public void invokeApplyItemArmAttackTransform(PoseStack poseStack, HumanoidArm humanoidArm, float f);
}
