package io.unlegit.modules.settings.impl;

import io.unlegit.modules.settings.Setting;

public class TextSetting extends Setting
{
    public String text;
    
    public TextSetting(String name, String text)
    {
        super(name);
        this.text = text;
    }
}
