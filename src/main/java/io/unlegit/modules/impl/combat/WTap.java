package io.unlegit.modules.impl.combat;

import io.unlegit.events.impl.entity.AttackE;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.utils.network.Packets;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;

@IModule(name = "W-Tap", description = "Deals more knockback to entities.")
public class WTap extends ModuleU
{
    public void onAttack(AttackE e)
    {
        if (mc.player.isSprinting())
        {
            command(ServerboundPlayerCommandPacket.Action.STOP_SPRINTING);
            command(ServerboundPlayerCommandPacket.Action.START_SPRINTING);
        }
    }

    public void command(ServerboundPlayerCommandPacket.Action action)
    {
        Packets.send(new ServerboundPlayerCommandPacket(mc.player, action));
    }
}
