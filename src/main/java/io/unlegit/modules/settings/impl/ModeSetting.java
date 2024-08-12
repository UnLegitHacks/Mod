package io.unlegit.modules.settings.impl;

import io.unlegit.modules.settings.Setting;

public class ModeSetting extends Setting
{
    private Runnable runnable;
    public String[] modes;
    public String mode;
    public int index;
    
    public ModeSetting(String name, String description, String[] modes)
    {
        super(name, description);
        this.modes = modes;
        mode = modes[0];
    }
    
    public boolean equals(String mode)
    {
        return this.mode.equals(mode);
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
