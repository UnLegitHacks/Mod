package io.unlegit.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import io.unlegit.UnLegit;
import net.minecraft.client.Minecraft;

@Mixin(Minecraft.class)
public class MinecraftMixin
{
//    @Inject(method = "setScreen", at = @At(value = "HEAD"))
//    private void useUnTitleScreen(CallbackInfo info, @Local LocalRef<Screen> screen)
//    {
//        if (screen.get() instanceof TitleScreen) screen.set(new UnTitleScreen());
//    }
    
    @ModifyVariable(method = "createTitle", at = @At(value = "STORE"), ordinal = 0)
    private StringBuilder insertClientTitle(StringBuilder builder)
    {
        return builder.insert(0, UnLegit.NAME + " - ");
    }
}