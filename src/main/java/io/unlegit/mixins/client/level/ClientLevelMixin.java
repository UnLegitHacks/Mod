package io.unlegit.mixins.client.level;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.unlegit.UnLegit;
import io.unlegit.modules.impl.render.TrueSight;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientLevel.class)
public class ClientLevelMixin
{
    @ModifyReturnValue(method = "getMarkerParticleTarget", at = @At(value = "RETURN"))
    private Block checkBarrier(Block block)
    {
        TrueSight trueSight = (TrueSight) UnLegit.modules.get("True Sight");
        return trueSight.isEnabled() && trueSight.barriers.enabled && block == null ? Blocks.BARRIER : block;
    }
}
