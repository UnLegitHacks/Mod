package io.unlegit.utils;

import java.util.ArrayList;
import java.util.Comparator;

import io.unlegit.interfaces.IMinecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
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
                
                if (distanceToEntity(target, player) <= distance && valid(target, player))
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
                targets.sort(Comparator.comparingDouble(entity -> distanceToEntity(entity, player)));
                break;
                
            case "Health":
                targets.sort(Comparator.comparingDouble(entity -> entity.getHealth()));
                break;
        }
        
        return targets.get(0);
    }
    
    public static boolean valid(LivingEntity entity, LocalPlayer player)
    {
//        return !entity.isEntityEqual(player) && entity.isEntityAlive() && !entity.isInvisibleToPlayer(player)
//                && (!entity.isOnSameTeam(player) || !UnLegit.CLIENT.moduleManager.getModule("Teams").isEnabled())
//                && (!NoBot.isBot(entity) || !UnLegit.CLIENT.moduleManager.getModule("No Bot").isEnabled());
        return !entity.is(player) && entity.isAlive() && !entity.isInvisibleTo(player);
    }
    
    public static float distanceToEntity(LivingEntity entity, LocalPlayer player)
    {
        double f = entity.getX() - player.getX();
        double f1 = entity.getY() - player.getY();
        double f2 = entity.getZ() - player.getZ();
        return Mth.sqrt((float) (f * f + f1 * f1 + f2 * f2));
    }
}
