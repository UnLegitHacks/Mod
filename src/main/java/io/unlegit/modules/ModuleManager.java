package io.unlegit.modules;

import java.util.ArrayList;

import io.unlegit.modules.impl.player.AutoSprint;

public class ModuleManager
{
    private ArrayList<ModuleU> modules = new ArrayList<>();
    
    public ModuleManager()
    {
        add(new AutoSprint());
        modules.get(0).setEnabled(true);
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
    
    public void add(ModuleU... modules)
    {
        for (ModuleU module : modules) this.modules.add(module);
    }
    
    public ArrayList<ModuleU> get() { return modules; }
}
