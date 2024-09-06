package io.unlegit.mixins.client;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import io.unlegit.UnLegit;
import io.unlegit.events.impl.client.UpdateE;
import io.unlegit.events.impl.entity.AttackE;
import io.unlegit.gui.UnThemeScreen;
import io.unlegit.gui.UnTitleScreen;
import io.unlegit.gui.font.IFont;
import io.unlegit.modules.impl.combat.killaura.AutoBlock;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

@Mixin(Minecraft.class)
public class MinecraftMixin
{
    @Shadow @Nullable public HitResult hitResult;
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
        if (screen.get() instanceof TitleScreen && "Fancy".equals(UnLegit.THEME))
            screen.set(UnLegit.isFirstLaunch() ? new UnThemeScreen() : new UnTitleScreen());
    }
    
    @Inject(method = "createTitle", at = @At(value = "HEAD"), cancellable = true)
    private void insertClientTitle(CallbackInfoReturnable<String> infoReturnable)
    {
        String title = UnLegit.NAME + " - MC " + SharedConstants.getCurrentVersion().getName();
        infoReturnable.setReturnValue(title);
    }
    
    @Inject(method = "startUseItem", at = @At(value = "HEAD"))
    public void fixEntityHitResult(CallbackInfo info)
    {
        if (AutoBlock.blockingTarget != null) hitResult = new EntityHitResult(AutoBlock.blockingTarget);
    }
    
    @Inject(method = "updateFontOptions", at = @At(value = "TAIL"))
    private void initializeFont(CallbackInfo info) { IFont.init(); }
}