package io.unlegit.modules.impl.combat;

import io.unlegit.events.impl.entity.AttackE;
import io.unlegit.events.impl.network.PacketSendE;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.impl.ModeSetting;
import io.unlegit.modules.settings.impl.SliderSetting;
import io.unlegit.utils.network.Packets;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Legit reach works by making the server think you
 * are lagging, like a cheap version of BackTrack.
 */
@IModule(name = "Reach", description = "Allows you to interact with entities from a greater range.")
public class Reach extends ModuleU
{
    public SliderSetting combatReach = new SliderSetting("Combat Reach", "The reach for combat.", 3, 3, 6);
    public ModeSetting mode = new ModeSetting("Mode", "The mode for reach.", new String[] {"Vanilla", "Legit"});
    private final Deque<Packet<?>> packets = new ConcurrentLinkedDeque<>();
    private Integer attackTick = null;

    public void onAttack(AttackE e)
    {
        if (mode.equals("Legit"))
            attackTick = mc.player.tickCount;
    }

    public void onUpdate()
    {
        if (mode.equals("Legit"))
        {
            int delay = (int) ((combatReach.value - 3) * 5);

            if (mc.player.tickCount % delay == 0)
            {
                while (!packets.isEmpty())
                    Packets.sendNoEvent(packets.poll());
            }
        }
    }

    public void onPacketSend(PacketSendE e)
    {
        if (attackTick != null && (mc.player.tickCount - attackTick) < 15 && !(e.packet instanceof ServerboundInteractPacket) && !(e.packet instanceof ServerboundSwingPacket))
        {
            packets.add(e.packet);
            e.cancelled = true;
        }
    }

    public void onWorldChange()
    {
        attackTick = null;
    }
}
