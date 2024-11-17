package io.unlegit.mixins.item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import io.unlegit.UnLegit;
import io.unlegit.modules.impl.item.OldHitting;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.ItemUseAnimation;

@Mixin(ItemInHandRenderer.class)
public class ItemRenderMixin
{
    @Inject(method = "renderArmWithItem", at = @At(value = "HEAD"), cancellable = true)
    private void hideShield(AbstractClientPlayer abstractClientPlayer, float f, float g, InteractionHand interactionHand, float swingProgress, ItemStack itemStack, float equipProgress, PoseStack poseStack, MultiBufferSource multiBufferSource, int j, CallbackInfo info)
    {
        OldHitting oldHitting = (OldHitting) UnLegit.modules.get("Old Hitting");
        
        if (oldHitting.isEnabled() && interactionHand == InteractionHand.OFF_HAND
                && itemStack.is(Items.SHIELD) && abstractClientPlayer.getMainHandItem().getItem()
                instanceof SwordItem) info.cancel();
    }
    
    @Inject(method = "renderArmWithItem", slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getUseAnimation()Lnet/minecraft/world/item/ItemUseAnimation;")), at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;applyItemArmTransform(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/entity/HumanoidArm;F)V", ordinal = 2, shift = At.Shift.AFTER))
    private void blockingAnimation(AbstractClientPlayer abstractClientPlayer, float f, float g, InteractionHand interactionHand, float swingProgress, ItemStack itemStack, float equipProgress, PoseStack poseStack, MultiBufferSource multiBufferSource, int j, CallbackInfo info)
    {
        OldHitting oldHitting = (OldHitting) UnLegit.modules.get("Old Hitting");
        
        if (oldHitting.isEnabled() && interactionHand == InteractionHand.MAIN_HAND)
            oldHitting.performAnimation(poseStack, swingProgress, equipProgress);
    }
    
    @Redirect(method = "renderArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getUseAnimation()Lnet/minecraft/world/item/ItemUseAnimation;"))
    private ItemUseAnimation swordBlockingAction(ItemStack itemStack)
    {
        OldHitting oldHitting = (OldHitting) UnLegit.modules.get("Old Hitting");
        
        if (oldHitting.isEnabled() && oldHitting.playerBlocking() && itemStack.getItem() instanceof SwordItem)
            return ItemUseAnimation.BLOCK;
        else return itemStack.getUseAnimation();
    }
    
    @Redirect(method = "renderArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/AbstractClientPlayer;isUsingItem()Z", ordinal = 1))
    private boolean blockingSwordUseAction(AbstractClientPlayer abstractClientPlayer)
    {
        OldHitting oldHitting = (OldHitting) UnLegit.modules.get("Old Hitting");
        
        if (oldHitting.isEnabled() && oldHitting.playerBlocking() && abstractClientPlayer.getMainHandItem().getItem() instanceof SwordItem)
            return true;
        else return abstractClientPlayer.isUsingItem();
    }
    
    @Redirect(method = "renderArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/AbstractClientPlayer;getUseItemRemainingTicks()I", ordinal = 2))
    private int blockingSwordTicks(AbstractClientPlayer abstractClientPlayer)
    {
        OldHitting oldHitting = (OldHitting) UnLegit.modules.get("Old Hitting");
        
        if (oldHitting.isEnabled() && oldHitting.playerBlocking() && abstractClientPlayer.getMainHandItem().getItem() instanceof SwordItem)
            return 1;
        else return abstractClientPlayer.getUseItemRemainingTicks();
    }
    
    @Redirect(method = "renderArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/AbstractClientPlayer;getUsedItemHand()Lnet/minecraft/world/InteractionHand;", ordinal = 1))
    private InteractionHand hookActiveHand(AbstractClientPlayer abstractClientPlayer)
    {
        OldHitting oldHitting = (OldHitting) UnLegit.modules.get("Old Hitting");
        
        if (oldHitting.isEnabled() && oldHitting.playerBlocking() && abstractClientPlayer.getMainHandItem().getItem() instanceof SwordItem)
            return InteractionHand.MAIN_HAND;
        else return abstractClientPlayer.getUsedItemHand();
    }
}
