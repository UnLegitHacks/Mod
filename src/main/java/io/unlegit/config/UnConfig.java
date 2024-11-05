package io.unlegit.config;

import com.google.gson.*;
import io.unlegit.UnLegit;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.Setting;
import io.unlegit.modules.settings.SettingManager;
import io.unlegit.modules.settings.impl.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Map.Entry;

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
                if (setting instanceof ToggleSetting toggleSetting)
                    jsonModule.addProperty(setting.name, toggleSetting.enabled);
                
                else if (setting instanceof SliderSetting sliderSetting)
                    jsonModule.addProperty(setting.name, sliderSetting.value);
                
                else if (setting instanceof ColorSetting colorSetting)
                    jsonModule.addProperty(setting.name, colorSetting.red + ";" + colorSetting.green + ";" + colorSetting.blue + ";" + colorSetting.alpha + ";" + colorSetting.rainbow);
                
                else if (setting instanceof TextSetting textSetting)
                    jsonModule.addProperty(setting.name, textSetting.text);
                
                else if (setting instanceof ModeSetting modeSetting)
                    jsonModule.addProperty(setting.name, modeSetting.selected);
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

            for (Entry<String, JsonElement> entry : jsonObject.entrySet())
            {
                ModuleU module = UnLegit.modules.get(entry.getKey());
                if (module == null) continue; // Forward compatibility
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
                        if (element == null) continue; // Forward compatibility

                        switch (setting)
                        {
                            case ToggleSetting toggleSetting ->
                            {
                                toggleSetting.enabled = element.getAsBoolean();
                                toggleSetting.onChange();
                            }

                            case SliderSetting sliderSetting -> sliderSetting.value = element.getAsFloat();

                            case ColorSetting colorSetting ->
                            {
                                String[] colors = element.getAsString().split(";");
                                colorSetting.red = Integer.parseInt(colors[0]);
                                colorSetting.green = Integer.parseInt(colors[1]);
                                colorSetting.blue = Integer.parseInt(colors[2]);
                                colorSetting.alpha = Integer.parseInt(colors[3]);
                                colorSetting.rainbow = Boolean.parseBoolean(colors[4]);
                            }

                            case TextSetting textSetting -> textSetting.text = element.getAsString();

                            case ModeSetting modeSetting ->
                            {
                                modeSetting.selected = element.getAsString();
                                modeSetting.onChange();
                            }

                            default -> {}
                        }
                    }
                }

                module.settingsReload();
            }
        }

        catch (Exception e) {}
    }
    
    public static void init()
    {
        if (config.exists()) load();
        else save();
    }
}