package io.unlegit.modules;

import java.util.ArrayList;

import io.unlegit.UnLegit;
import io.unlegit.modules.impl.combat.*;
import io.unlegit.modules.impl.combat.killaura.KillAura;
import io.unlegit.modules.impl.gui.*;
import io.unlegit.modules.impl.gui.keystrokes.KeyStrokes;
import io.unlegit.modules.impl.item.*;
import io.unlegit.modules.impl.misc.*;
import io.unlegit.modules.impl.movement.*;
import io.unlegit.modules.impl.movement.scaffold.Scaffold;
import io.unlegit.modules.impl.player.*;
import io.unlegit.modules.impl.render.*;
import io.unlegit.modules.impl.world.TimeChanger;
import io.unlegit.tracker.PlayerTracker;

public class ModuleManager
{
    private ArrayList<ModuleU> modules = new ArrayList<>();
    
    public ModuleManager()
    {
        add(new AutoSprint(), new Speed(), new AutoTotem(), new Cooldown(),
            new AutoRespawn(), new ActiveMods(), new KillAura(), new FPSBooster(),
            new KeepSprint(), new Criticals(), new Velocity(), new ChestStealer(),
            new OldHitting(), new FullBright(), new ESP(), new NameTags(),
            new InvMove(), new SafeWalk(), new KeyStrokes(), new GamePlay(),
            new Scaffold(), new Spammer(), new AntiBlind(), new LowFire(),
            new NoSlow(), new Compass(), new Scoreboard(), new NoteBot(),
            new NoHurtCam(), new TimeChanger(), new WTap(), new Blink(),
            new Fly(), new Rotations(), new NoFall(), new MurderMystery(),
            new AutoArmor(), new TabGui(), new Reach(), new Minimap(),
            new PacketBlocker(), new Jargon());
        
        PlayerTracker.get().start();
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
