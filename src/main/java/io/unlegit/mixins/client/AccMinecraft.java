package io.unlegit.mixins.client;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Minecraft.class)
public interface AccMinecraft
{
    @Invoker("startAttack")
    boolean invokeStartAttack();
}
