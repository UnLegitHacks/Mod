package io.unlegit.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import io.unlegit.UnLegit;
import io.unlegit.modules.impl.player.InvMove;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.player.KeyboardInput;

@Mixin(KeyboardInput.class)
public class KeyInputMixin
{
    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "isDown"))
    public boolean keyDownInvMove(KeyMapping key)
    {
        InvMove invMove = (InvMove) UnLegit.modules.get("Inv Move");
        return invMove.isEnabled() ? invMove.canMove(key) : key.isDown();
    }
}
