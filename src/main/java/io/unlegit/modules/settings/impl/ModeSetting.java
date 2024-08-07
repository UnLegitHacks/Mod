package io.unlegit.modules.settings.impl;

import io.unlegit.modules.settings.Setting;

public class ModeSetting extends Setting
{
    private Runnable runnable;
    public String currentMode;
    public String[] modes;
    public int index;
    
    public ModeSetting(String name, String[] modes)
    {
        super(name);
        this.modes = modes;
        this.currentMode = modes[0];
    }
    
    public boolean equals(String mode)
    {
        return currentMode.equals(mode);
    }
    
    public void onChange()
    {
        if (runnable != null) runnable.run();
    }
    
    public void setAction(Runnable runnable)
    {
        this.runnable = runnable;
    }
}
