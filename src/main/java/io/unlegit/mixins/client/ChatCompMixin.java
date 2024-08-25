package io.unlegit.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.unlegit.UnLegit;
import io.unlegit.events.impl.client.MessageE;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;

@Mixin(ChatComponent.class)
public class ChatCompMixin
{
    @Inject(method = "addMessage", at = @At(value = "HEAD"))
    public void hookMessageEvent(Component component, CallbackInfo info)
    {
        UnLegit.events.post(MessageE.get(component));
    }
}
