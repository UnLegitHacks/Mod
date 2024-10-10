package io.unlegit.mixins.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import io.unlegit.UnLegit;
import net.minecraft.client.gui.screens.ChatScreen;

@Mixin(ChatScreen.class)
public class ChatMixin
{
    @Inject(method = "handleChatInput", at = @At("HEAD"), cancellable = true)
    public void handleChatInput(CallbackInfo info, @Local LocalRef<String> string)
    {
        if (string.get().startsWith("."))
        {
            UnLegit.commands.handle(string.get());
            info.cancel();
        }
    }
}
