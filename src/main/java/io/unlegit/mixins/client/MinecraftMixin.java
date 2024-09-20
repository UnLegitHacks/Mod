package io.unlegit.mixins.client;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.platform.Window;

import io.unlegit.UnLegit;
import io.unlegit.events.impl.client.UpdateE;
import io.unlegit.events.impl.client.WorldChangeE;
import io.unlegit.events.impl.entity.AttackE;
import io.unlegit.gui.UnThemeScreen;
import io.unlegit.gui.UnTitleScreen;
import io.unlegit.gui.font.IFont;
import io.unlegit.modules.impl.combat.killaura.AutoBlock;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

@Mixin(Minecraft.class)
public class MinecraftMixin
{
    @Shadow @Nullable public HitResult hitResult;
    @Shadow private volatile boolean pause;
    @Shadow @Final private Window window;
    @Shadow public LocalPlayer player;
    
    @Inject(method = "tick", at = @At(value = "HEAD"))
    public void updateEvent(CallbackInfo info)
    {
        if (player != null && !pause)
            UnLegit.events.post(UpdateE.get());
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
        if (screen.get() instanceof TitleScreen)
        {
            if (UnLegit.isFirstLaunch())
                screen.set(new UnThemeScreen());
            else if ("Fancy".equals(UnLegit.THEME))
                screen.set(new UnTitleScreen());
        } else if (screen.get() instanceof UnTitleScreen && "Vanilla".equals(UnLegit.THEME))
            screen.set(new TitleScreen());
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
    
    @ModifyConstant(method = "getFramerateLimit", constant = @Constant(intValue = 60))
    private int getFramerateLimit(int frames)
    {
        return window.getFramerateLimit();
    }
    
    @Inject(method = "setLevel", at = @At(value = "TAIL"))
    public void worldChangeEvent(ClientLevel clientLevel, ReceivingLevelScreen.Reason reason, CallbackInfo info)
    {
        UnLegit.events.post(WorldChangeE.get());
    }
}