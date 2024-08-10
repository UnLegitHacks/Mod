package io.unlegit.modules.impl.combat.killaura;

import io.unlegit.interfaces.IMinecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.phys.EntityHitResult;

public class AutoBlock implements IMinecraft
{
    public static void block(LivingEntity target)
    {
        if (mc.player.getMainHandItem().getItem() instanceof SwordItem)
        {
            InteractionResult result = mc.gameMode.interactAt(mc.player, target, new EntityHitResult(target), InteractionHand.MAIN_HAND);
            if (!result.consumesAction()) result = mc.gameMode.interact(mc.player, target, InteractionHand.MAIN_HAND);
            if (result.consumesAction() && result.shouldSwing()) mc.player.swing(InteractionHand.MAIN_HAND);
        }
    }
    
    public static void unblock() { mc.player.releaseUsingItem(); }
}
