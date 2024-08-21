package io.unlegit.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import io.unlegit.UnLegit;
import io.unlegit.events.impl.client.UpdateE;
import io.unlegit.events.impl.entity.AttackE;
import io.unlegit.gui.UnTitleScreen;
import io.unlegit.gui.font.IFont;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.player.LocalPlayer;

@Mixin(Minecraft.class)
public class MinecraftMixin
{
    @Shadow private volatile boolean pause;
    @Shadow public LocalPlayer player;
    
    @Inject(method = "tick", at = @At(value = "HEAD"))
    public void updateEvent(CallbackInfo info)
    {
        if (player != null && !pause) UnLegit.events.post(UpdateE.get());
    }
    
    @Inject(method = "startAttack", at = @At(value = "HEAD"), cancellable = true)
    public void attackEvent(CallbackInfoReturnable<Boolean> infoReturnable)
    {
        AttackE e = AttackE.get();
        UnLegit.events.post(e);
        if (e.cancelled) infoReturnable.setReturnValue(false);
    }
    
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