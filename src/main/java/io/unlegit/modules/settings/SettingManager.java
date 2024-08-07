package io.unlegit.modules.settings;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import io.unlegit.modules.ModuleManager;
import io.unlegit.modules.ModuleU;

public class SettingManager
{
    private HashMap<ModuleU, List<Setting>> settings = new HashMap<>();
    
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
                for (Setting setting : entry.getValue()) result.add(setting);
            }
        }
        
        return result;
    }
    
    /**
     * Automatically registers settings, how cool is that!
     */
    private void registerSettings(ModuleU module)
    {
        ArrayList<Setting> settings = new ArrayList<>();
        
        for (Field field : module.getClass().getDeclaredFields())
        {
            try {
                if (field.getType().getSimpleName().endsWith("Setting"))
                    settings.add((Setting) field.get(module)); } catch (Exception e) {}
        }
        
        this.settings.put(module, settings);
    }
}
