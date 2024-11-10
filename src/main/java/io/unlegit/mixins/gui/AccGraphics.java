package io.unlegit.mixins.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiGraphics.class)
public interface AccGraphics
{
    @Mutable @Accessor("pose")
    void setPose(PoseStack pose);

    @Accessor
    MultiBufferSource.BufferSource getBufferSource();
}
