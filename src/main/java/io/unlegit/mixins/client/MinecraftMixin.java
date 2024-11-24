package io.unlegit.mixins.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.platform.Window;
import io.unlegit.UnLegit;
import io.unlegit.events.impl.client.UpdateE;
import io.unlegit.events.impl.client.WorldChangeE;
import io.unlegit.events.impl.entity.AttackE;
import io.unlegit.gui.UnThemePicker;
import io.unlegit.gui.UnTitleScreen;
import io.unlegit.gui.font.IFont;
import io.unlegit.modules.impl.combat.killaura.AutoBlock;
import io.unlegit.modules.impl.render.ESP;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
    private void useUnTitleScreen(CallbackInfo info, @Local(argsOnly = true) LocalRef<Screen> screen)
    {
        if (screen.get() instanceof TitleScreen)
        {
            if (UnLegit.isFirstLaunch())
                screen.set(new UnThemePicker());
            else if ("Fancy".equals(UnLegit.THEME))
                screen.set(new UnTitleScreen());
        } else if (screen.get() instanceof UnTitleScreen && "Vanilla".equals(UnLegit.THEME))
            screen.set(new TitleScreen());
    }
    
    @Inject(method = "createTitle", at = @At(value = "HEAD"), cancellable = true)
    private void insertClientTitle(CallbackInfoReturnable<String> infoReturnable)
    {
        String title = "UL Hacks " + UnLegit.VERSION + " - MC " + SharedConstants.getCurrentVersion().getName();
        infoReturnable.setReturnValue(title);
    }
    
    @Inject(method = "startUseItem", at = @At(value = "HEAD"))
    public void fixEntityHitResult(CallbackInfo info)
    {
        if (AutoBlock.blockingTarget != null) hitResult = new EntityHitResult(AutoBlock.blockingTarget);
    }
    
    @Inject(method = "updateFontOptions", at = @At(value = "TAIL"))
    private void initializeFont(CallbackInfo info) { IFont.init(); }

    @Inject(method = "setLevel", at = @At(value = "TAIL"))
    public void worldChangeEvent(ClientLevel clientLevel, ReceivingLevelScreen.Reason reason, CallbackInfo info)
    {
        UnLegit.events.post(WorldChangeE.get());
    }

    @Inject(method = "shouldEntityAppearGlowing", at = @At(value = "HEAD"), cancellable = true)
    public void shouldEntityAppearGlowing(Entity entity, CallbackInfoReturnable<Boolean> glowing)
    {
        ESP esp = (ESP) UnLegit.modules.get("ESP");

        if (esp.isEnabled() && esp.mode.equals("Outline") && entity instanceof LivingEntity && !(entity instanceof LocalPlayer) && !entity.isInvisibleTo(player))
            glowing.setReturnValue(true);
    }
}