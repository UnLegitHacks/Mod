package io.unlegit.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.unlegit.UnLegit;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.Setting;
import io.unlegit.modules.settings.SettingManager;
import io.unlegit.modules.settings.impl.ColorSetting;
import io.unlegit.modules.settings.impl.ModeSetting;
import io.unlegit.modules.settings.impl.SliderSetting;
import io.unlegit.modules.settings.impl.TextSetting;
import io.unlegit.modules.settings.impl.ToggleSetting;

public class UnConfig
{
    private static final Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
    public static final File config = new File("unlegit/config.json");
    
    public static void saveModules()
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
            jsonModule.addProperty("Enabled", module.isEnabled());
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
    
    public static void loadModules()
    {
        SettingManager settingManager = UnLegit.settings;
        
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(config)))
        {
            JsonObject jsonObject = (JsonObject) JsonParser.parseReader(bufferedReader);
            Iterator<Entry<String, JsonElement>> iterator = jsonObject.entrySet().iterator();
            
            while (iterator.hasNext())
            {
                Entry<String, JsonElement> entry = iterator.next();
                ModuleU module = UnLegit.modules.get(entry.getKey().toString());
                if (module == null) continue; // Forward compatibility handling
                JsonObject jsonModule = (JsonObject) entry.getValue();
                
                if (jsonModule.get("Enabled").getAsBoolean()) module.setEnabled(true);
                else if (module.isEnabled()) module.setEnabled(false);
                
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
        } catch (Exception e) {}
    }
    
    public static void init()
    {
        if (config.exists()) loadModules();
        else saveModules();
    }
}