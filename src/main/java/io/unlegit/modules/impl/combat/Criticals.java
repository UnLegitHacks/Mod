package io.unlegit.modules.impl.combat;

import io.unlegit.UnLegit;
import io.unlegit.events.impl.AttackE;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.impl.combat.killaura.KillAura;

@IModule(name = "Criticals", description = "Always does critical hits when you attack someone.")
public class Criticals extends ModuleU
{
    public void onAttack(AttackE e)
    {
        KillAura killAura = (KillAura) UnLegit.modules.get("Kill Aura");
        if (mc.player.onGround() && !mc.options.keyJump.isDown() && (mc.crosshairPickEntity != null || (killAura.isEnabled() && killAura.target != null))) mc.player.jumpFromGround();
    }
}
