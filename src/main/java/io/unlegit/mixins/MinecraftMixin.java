package io.unlegit.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import io.unlegit.UnLegit;
import io.unlegit.gui.UnTitleScreen;
import io.unlegit.gui.font.IFont;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;

@Mixin(Minecraft.class)
public class MinecraftMixin
{
    @Inject(method = "setScreen", at = @At(value = "HEAD"))
    private void useUnTitleScreen(CallbackInfo info, @Local LocalRef<Screen> screen)
    {
        if (screen.get() instanceof TitleScreen) screen.set(new UnTitleScreen());
    }
    
    @ModifyVariable(method = "createTitle", at = @At(value = "STORE"), ordinal = 0)
    private StringBuilder insertClientTitle(StringBuilder builder)
    {
        return builder.insert(0, UnLegit.NAME + " - ");
    }
    
    @Inject(method = "updateFontOptions", at = @At(value = "TAIL"))
    private void initializeFont(CallbackInfo info) { IFont.init(); }
}