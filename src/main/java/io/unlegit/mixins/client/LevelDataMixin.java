package io.unlegit.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import io.unlegit.UnLegit;
import io.unlegit.modules.impl.world.TimeChanger;
import net.minecraft.client.multiplayer.ClientLevel;

@Mixin(ClientLevel.ClientLevelData.class)
public class LevelDataMixin
{
    @ModifyReturnValue(method = "getDayTime", at = @At(value = "RETURN"))
    public long timeChangerModule(long time)
    {
        TimeChanger timeChanger = (TimeChanger) UnLegit.modules.get("Time Changer");
        return timeChanger.isEnabled() ? (long) timeChanger.time.value : time;
    }
}
