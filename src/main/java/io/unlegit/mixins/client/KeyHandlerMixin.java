package io.unlegit.mixins.client;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.unlegit.UnLegit;
import io.unlegit.events.impl.client.KeyE;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;

@Mixin(KeyboardHandler.class)
public class KeyHandlerMixin
{
    @Shadow @Final
    private Minecraft minecraft;
    
    @Inject(method = "keyPress", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;screen:Lnet/minecraft/client/gui/screens/Screen;", ordinal = 0))
    public void keyEvent(long window, int key, int scancode, int i, int j, CallbackInfo info)
    {
        if (minecraft.screen == null && i == 1) UnLegit.events.post(KeyE.get(key));
    }
}
