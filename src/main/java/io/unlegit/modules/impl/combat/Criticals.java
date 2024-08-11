package io.unlegit.modules.impl.combat;

import io.unlegit.events.impl.AttackE;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;

@IModule(name = "Criticals", description = "Always does critical hits when you attack someone.")
public class Criticals extends ModuleU
{
    public void onAttack(AttackE e)
    {
        if (mc.player.onGround() && !mc.options.keyJump.isDown()) mc.player.jumpFromGround();
    }
}
