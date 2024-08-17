package io.unlegit.mixins.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;

@Mixin(GameRenderer.class)
public interface AccGameRender
{
    @Accessor
    PostChain getBlurEffect();
}
