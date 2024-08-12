package io.unlegit.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import io.unlegit.UnLegit;
import io.unlegit.modules.impl.player.OldHitting;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin
{   
    @Inject(method = "renderArmWithItem", at = @At(value = "HEAD"), cancellable = true)
    public void hideShield(AbstractClientPlayer abstractClientPlayer, float f, float g, InteractionHand interactionHand, float swingProgress, ItemStack itemStack, float equipProgress, PoseStack poseStack, MultiBufferSource multiBufferSource, int j, CallbackInfo info)
    {
        OldHitting oldHitting = (OldHitting) UnLegit.modules.get("Old Hitting");
        
        if (oldHitting.isEnabled() && interactionHand == InteractionHand.OFF_HAND
                && itemStack.is(Items.SHIELD) && abstractClientPlayer.getMainHandItem().getItem()
                instanceof SwordItem) info.cancel();
    }
    
    @Inject(method = "renderArmWithItem", slice = @Slice(from = @At(value = "INVOKE", target = "getUseAnimation")), at = @At(value = "INVOKE", target = "applyItemArmTransform", shift = At.Shift.AFTER))
    public void blockingAnimation(AbstractClientPlayer abstractClientPlayer, float f, float g, InteractionHand interactionHand, float swingProgress, ItemStack itemStack, float equipProgress, PoseStack poseStack, MultiBufferSource multiBufferSource, int j, CallbackInfo info)
    {
        OldHitting oldHitting = (OldHitting) UnLegit.modules.get("Old Hitting");
        
        if (oldHitting.isEnabled() && oldHitting.playerBlocking() && interactionHand == InteractionHand.MAIN_HAND)
            oldHitting.performAnimation(poseStack, swingProgress, equipProgress);
    }
}
