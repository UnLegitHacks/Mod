package io.unlegit.modules;

import java.util.ArrayList;

import io.unlegit.UnLegit;
import io.unlegit.modules.impl.combat.killaura.KillAura;
import io.unlegit.modules.impl.gui.ActiveMods;
import io.unlegit.modules.impl.item.AutoTotem;
import io.unlegit.modules.impl.movement.Speed;
import io.unlegit.modules.impl.player.*;
import io.unlegit.modules.impl.render.FPSBooster;

public class ModuleManager
{
    private ArrayList<ModuleU> modules = new ArrayList<>();
    
    public ModuleManager()
    {
        add(new AutoSprint(), new Speed(), new AutoTotem(), new Cooldown(),
            new AutoRespawn(), new ActiveMods(), new KillAura(), new FPSBooster());
        UnLegit.settings.call(this);
    }
    
    public ArrayList<ModuleU> get(CategoryM category)
    {
        ArrayList<ModuleU> modules = new ArrayList<>();
        
        for (ModuleU module : this.modules)
        {
            if (module.category.equals(category))
                modules.add(module);
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