package io.unlegit.mixins.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.GuiGraphics;

@Mixin(GuiGraphics.class)
public interface AccGraphics
{
    @Mutable @Accessor("pose")
    public void setPose(PoseStack pose);
}
