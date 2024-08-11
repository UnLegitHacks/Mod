package io.unlegit.modules.settings.impl;

import io.unlegit.modules.settings.Setting;

public class SliderSetting extends Setting
{
    public float minValue, currentValue, maxValue;
    
    public SliderSetting(String name, String description, float minValue, float currentValue, float maxValue)
    {
        super(name, description);
        this.minValue = minValue;
        this.currentValue = currentValue;
        this.maxValue = maxValue;
    }
}
