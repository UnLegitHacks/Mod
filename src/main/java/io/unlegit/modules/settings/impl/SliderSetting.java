package io.unlegit.modules.settings.impl;

import io.unlegit.modules.settings.Setting;

public class SliderSetting extends Setting
{
    public float minValue, value, maxValue;
    
    public SliderSetting(String name, String description, float minValue, float value, float maxValue)
    {
        super(name, description);
        this.minValue = minValue;
        this.value = value;
        this.maxValue = maxValue;
    }
}
