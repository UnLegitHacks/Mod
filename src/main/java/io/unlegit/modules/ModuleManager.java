package io.unlegit.modules;

import java.util.ArrayList;

import io.unlegit.UnLegit;
import io.unlegit.modules.impl.combat.Criticals;
import io.unlegit.modules.impl.combat.KeepSprint;
import io.unlegit.modules.impl.combat.Velocity;
import io.unlegit.modules.impl.combat.killaura.KillAura;
import io.unlegit.modules.impl.gui.ActiveMods;
import io.unlegit.modules.impl.gui.Compass;
import io.unlegit.modules.impl.gui.keystrokes.KeyStrokes;
import io.unlegit.modules.impl.item.AutoTotem;
import io.unlegit.modules.impl.item.ChestStealer;
import io.unlegit.modules.impl.misc.GamePlay;
import io.unlegit.modules.impl.misc.Spammer;
import io.unlegit.modules.impl.movement.*;
import io.unlegit.modules.impl.movement.scaffold.Scaffold;
import io.unlegit.modules.impl.player.*;
import io.unlegit.modules.impl.render.*;

public class ModuleManager
{
    private ArrayList<ModuleU> modules = new ArrayList<>();
    
    public ModuleManager()
    {
        add(new AutoSprint(), new Speed(), new AutoTotem(), new Cooldown(),
            new AutoRespawn(), new ActiveMods(), new KillAura(), new FPSBooster(),
            new KeepSprint(), new Criticals(), new Velocity(), new ChestStealer(),
            new OldHitting(), new FullBright(), new ESP(), new NameTags(),
            new InvMove(), new SafeWalk(), new KeyStrokes(), new Strafe(),
            new GamePlay(), new Scaffold(), new Spammer(), new AntiBlind(),
            new LowFire(), new NoSlow(), new Compass());
        UnLegit.settings.call(this);
    }
    
    public ArrayList<ModuleU> get(CategoryM category)
    {
        ArrayList<ModuleU> modules = new ArrayList<>();
        
        for (ModuleU module : this.modules)
        {
            if (module.category.equals(category)) modules.add(module);
        }
        
        return modules;
    }
    
    public ModuleU get(String name)
    {
        for (ModuleU module : this.modules)
        {
            if (module.name.replace(" ", "").equalsIgnoreCase(name.replace(" ", "")))
                return module;
        }
        
        return null;
    }
    
    public void add(ModuleU... modules)
    {
        for (ModuleU module : modules) this.modules.add(module);
    }
    
    public ArrayList<ModuleU> get() { return modules; }
}
