package io.unlegit.config;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

import com.google.gson.*;

import io.unlegit.UnLegit;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.Setting;
import io.unlegit.modules.settings.SettingManager;
import io.unlegit.modules.settings.impl.*;

public class UnConfig
{
    private static final Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
    public static final File config = new File("unlegit/config.json");
    
    public static void save()
    {
        if (!config.exists())
        {
            try
            {
                if (!config.getParentFile().exists()) config.getParentFile().mkdirs();
                config.createNewFile();
            } catch (IOException e) {}
        }
        
        SettingManager settingManager = UnLegit.settings;
        JsonObject jsonObject = new JsonObject();
        
        for (ModuleU module : UnLegit.modules.get())
        {
            JsonObject jsonModule = new JsonObject();
            jsonObject.add(module.name, jsonModule);
            jsonModule.addProperty("Enabled", module.isEnabled() && !module.noStart);
            jsonModule.addProperty("Hidden", module.hidden);
            jsonModule.addProperty("Key", module.key);
            ArrayList<Setting> settings = settingManager.get(module);
            
            for (Setting setting : settings)
            {
                if (setting instanceof ToggleSetting)
                {
                    ToggleSetting toggleSetting = (ToggleSetting) setting;
                    jsonModule.addProperty(setting.name, toggleSetting.enabled);
                }
                
                else if (setting instanceof SliderSetting)
                {
                    SliderSetting sliderSetting = (SliderSetting) setting;
                    jsonModule.addProperty(setting.name, sliderSetting.value);
                }
                
                else if (setting instanceof ColorSetting)
                {
                    ColorSetting colorSetting = (ColorSetting) setting;
                    jsonModule.addProperty(setting.name, colorSetting.red + ";" + colorSetting.green + ";" + colorSetting.blue + ";" + colorSetting.alpha);
                }
                
                else if (setting instanceof TextSetting)
                {
                    TextSetting textSetting = (TextSetting) setting;
                    jsonModule.addProperty(setting.name, textSetting.text);
                }
                
                else if (setting instanceof ModeSetting)
                {
                    ModeSetting modeSetting = (ModeSetting) setting;
                    jsonModule.addProperty(setting.name, modeSetting.selected);
                }
            }
        }
        
        try (PrintWriter printWriter = new PrintWriter(new FileWriter(config)))
        {
            printWriter.println(prettyGson.toJson(jsonObject));
        } catch (IOException e) {}
    }
    
    public static void load()
    {
        SettingManager settingManager = UnLegit.settings;
        
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(config)))
        {
            JsonObject jsonObject = (JsonObject) JsonParser.parseReader(bufferedReader);
            Iterator<Entry<String, JsonElement>> iterator = jsonObject.entrySet().iterator();
            
            while (iterator.hasNext())
            {
                Entry<String, JsonElement> entry = iterator.next();
                
                ModuleU module = UnLegit.modules.get(entry.getKey());
                if (module == null) continue; // Forward compatibility handling
                JsonObject jsonModule = (JsonObject) entry.getValue();
                
                if (jsonModule.get("Enabled").getAsBoolean()) module.setEnabled(true);
                else if (module.isEnabled()) module.setEnabled(false);
                
                module.hidden = jsonModule.get("Hidden").getAsBoolean();
                module.key = jsonModule.get("Key").getAsInt();
                ArrayList<Setting> settings = settingManager.get(module);
                
                for (Setting setting : settings)
                {
                    if (setting != null)
                    {
                        JsonElement element = jsonModule.get(setting.name);
                        if (element == null) continue; // Forward compatibility handling
                        
                        if (setting instanceof ToggleSetting)
                        {
                            ToggleSetting toggleSetting = (ToggleSetting) setting;
                            toggleSetting.enabled = element.getAsBoolean();
                            toggleSetting.onChange();
                        }
                        
                        else if (setting instanceof SliderSetting)
                        {
                            SliderSetting sliderSetting = (SliderSetting) setting;
                            sliderSetting.value = element.getAsFloat();
                        }
                        
                        else if (setting instanceof ColorSetting)
                        {
                            ColorSetting colorSetting = (ColorSetting) setting;
                            String[] colors = element.getAsString().split(";");
                            colorSetting.red = Integer.parseInt(colors[0]);
                            colorSetting.green = Integer.parseInt(colors[1]);
                            colorSetting.blue = Integer.parseInt(colors[2]);
                            colorSetting.alpha = Integer.parseInt(colors[3]);
                        }
                        
                        else if (setting instanceof TextSetting)
                        {
                            TextSetting textSetting = (TextSetting) setting;
                            textSetting.text = element.getAsString();
                        }
                        
                        else if (setting instanceof ModeSetting)
                        {
                            ModeSetting modeSetting = (ModeSetting) setting;
                            modeSetting.selected = element.getAsString();
                            modeSetting.onChange();
                        }
                    }
                }
                
                module.settingsReload();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    public static void init()
    {
        if (config.exists()) load();
        else save();
    }
}