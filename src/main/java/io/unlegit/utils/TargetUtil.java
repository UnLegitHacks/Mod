package io.unlegit.utils;

import java.util.ArrayList;
import java.util.Comparator;

import io.unlegit.UnLegit;
import io.unlegit.interfaces.IMinecraft;
import io.unlegit.modules.impl.combat.killaura.KillAura;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class TargetUtil implements IMinecraft
{
    public static LivingEntity getTarget(LocalPlayer player, float distance, String priority)
    {
        ArrayList<LivingEntity> targets = new ArrayList<>();
        
        for (Entity entity : mc.level.entitiesForRendering())
        {
            if (entity instanceof LivingEntity)
            {
                LivingEntity target = (LivingEntity) entity;
                
                if (player.distanceTo(target) <= distance && valid(target, player))
                    targets.add(target);
            }
        }
        
        if (targets.isEmpty()) return null;
        
        switch (priority)
        {
            case "Hurt Time":
                targets.sort(Comparator.comparingInt(entity -> entity.hurtTime));
                break;
                
            case "Distance":
                targets.sort(Comparator.comparingDouble(entity -> player.distanceTo(entity)));
                break;
                
            case "Health":
                targets.sort(Comparator.comparingDouble(entity -> entity.getHealth()));
                break;
        }
        
        return targets.get(0);
    }
    
    public static boolean valid(LivingEntity entity, LocalPlayer player)
    {
        KillAura killAura = (KillAura) UnLegit.modules.get("Kill Aura");
//        return !entity.isEntityEqual(player) && entity.isEntityAlive() && !entity.isInvisibleToPlayer(player)
//                && (!entity.isOnSameTeam(player) || !UnLegit.CLIENT.moduleManager.getModule("Teams").isEnabled())
//                && (!NoBot.isBot(entity) || !UnLegit.CLIENT.moduleManager.getModule("No Bot").isEnabled());
        return !entity.is(player) && entity.isAlive() && !entity.isInvisibleTo(player)
                && (entity.getTeam() == null || player.getTeam() == null || (!entity.getTeam().isAlliedTo(player.getTeam()) || !killAura.teams.enabled));
    }
}
