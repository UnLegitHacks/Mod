package io.unlegit.modules.settings.impl;

import io.unlegit.modules.settings.Setting;

public class ColorSetting extends Setting
{
    public int red, green, blue, alpha;
    
    public ColorSetting(String name, int red, int green, int blue, int alpha)
    {
        super(name);
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }
}
