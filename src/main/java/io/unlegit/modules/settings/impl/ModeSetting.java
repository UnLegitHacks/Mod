package io.unlegit.modules.settings.impl;

import io.unlegit.modules.settings.Setting;

public class ModeSetting extends Setting
{
    private Runnable runnable;
    public String selected;
    public String[] modes;
    public int index;
    
    public ModeSetting(String name, String description, String[] modes)
    {
        super(name, description);
        this.modes = modes;
        selected = modes[0];
    }
    
    public boolean equals(String mode)
    {
        return selected.equals(mode);
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
