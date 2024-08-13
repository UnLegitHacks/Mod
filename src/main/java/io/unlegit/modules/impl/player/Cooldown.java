package io.unlegit.modules.impl.player;

import io.unlegit.UnLegit;
import io.unlegit.events.impl.entity.AttackE;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.impl.combat.killaura.KillAura;
import net.minecraft.world.phys.HitResult;

@IModule(name = "Cooldown", description = "Does not let you swing while the item is in a cooldown.")
public class Cooldown extends ModuleU
{
    public void onAttack(AttackE e)
    {
        if (cancelHit()) e.cancelled = true;
    }
    
    public boolean cancelHit()
    {
        float cooldown = mc.player.getAttackStrengthScale(0);
        if (!blockSelected() && cooldown != 1) return true;
        else return false;
    }
    
    public boolean blockSelected()
    {
        KillAura killAura = (KillAura) UnLegit.modules.get("Kill Aura");
        return !(killAura.isEnabled() && killAura.target != null) && mc.hitResult != null && mc.hitResult.getType() == HitResult.Type.BLOCK;
    }
}
