package io.unlegit.modules.settings.impl;

import java.awt.Color;

import io.unlegit.modules.settings.Setting;
import io.unlegit.utils.render.EzColor;

public class ColorSetting extends Setting
{
    public int red, green, blue, alpha;
    public boolean rainbow = false;
    
    public ColorSetting(String name, String description, int red, int green, int blue, int alpha)
    {
        super(name, description);
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }
    
    public int get()
    {
        return rainbow ? spectrum() : EzColor.RGB(red, green, blue, alpha);
    }
    
    public int spectrum()
    {
        return Color.getHSBColor(((System.currentTimeMillis() / 25) % 255) / 255F, 0.75F, 1).getRGB();
    }
}
