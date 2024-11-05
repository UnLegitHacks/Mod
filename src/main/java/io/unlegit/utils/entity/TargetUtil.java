package io.unlegit.utils.entity;

import io.unlegit.interfaces.IMinecraft;
import io.unlegit.modules.impl.misc.Teams;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Comparator;

public class TargetUtil implements IMinecraft
{
    public static LivingEntity getTarget(LocalPlayer player, float distance, String priority)
    {
        ArrayList<LivingEntity> targets = new ArrayList<>();
        
        for (Entity entity : mc.level.entitiesForRendering())
        {
            if (entity instanceof LivingEntity target)
            {
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
                targets.sort(Comparator.comparingDouble(player::distanceTo));
                break;
                
            case "Health":
                targets.sort(Comparator.comparingDouble(LivingEntity::getHealth));
                break;
        }

        if (hasMobs(targets))
        {
            // Prioritize players
            for (LivingEntity entity : targets)
            {
                if (entity instanceof Player)
                    return entity;
            }
        }

        return targets.getFirst();
    }
    
    public static boolean valid(LivingEntity entity, LocalPlayer player)
    {
        return !entity.is(player) && entity.isAlive() && !entity.isInvisibleTo(player)
                && !(entity instanceof Slime) && (entity.getTeam() == null || player.getTeam() == null
                || (!entity.getTeam().isAlliedTo(player.getTeam()) || !Teams.active()));
    }

    private static boolean hasMobs(ArrayList<LivingEntity> targets)
    {
        for (LivingEntity entity : targets)
        {
            if (!(entity instanceof Player))
                return true;
        }

        return false;
    }
}
