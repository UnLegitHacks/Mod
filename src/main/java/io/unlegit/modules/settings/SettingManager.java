package io.unlegit.modules.settings;

import io.unlegit.modules.ModuleManager;
import io.unlegit.modules.ModuleU;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class SettingManager
{
    private final HashMap<ModuleU, List<Setting>> settings = new HashMap<>();
    
    public void call(ModuleManager modules)
    {
        for (ModuleU module : modules.get()) registerSettings(module);
    }
    
    public ArrayList<Setting> get(ModuleU module)
    {
        ArrayList<Setting> result = new ArrayList<>();
        
        for (Entry<ModuleU, List<Setting>> entry : settings.entrySet())
        {
            if (module.equals(entry.getKey()))
            {
                result.addAll(entry.getValue());
            }
        }
        
        return result;
    }
    
    /** Automatically registers settings, how cool is that! */
    private void registerSettings(ModuleU module)
    {
        ArrayList<Setting> settings = new ArrayList<>();
        
        for (Field field : module.getClass().getDeclaredFields())
        {
            try
            {
                if (field.getType().getSimpleName().endsWith("Setting"))
                {
                    Setting setting = (Setting) field.get(module);
                    if (setting != null) settings.add(setting);
                }
            } catch (Exception e) {}
        }
        
        if (!this.settings.containsKey(module))
            putSettings(module, settings);
    }
    
    public void putSettings(ModuleU module, List<Setting> settings)
    {
        this.settings.put(module, settings);
    }
}
