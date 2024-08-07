package io.unlegit.modules.settings.impl;

import io.unlegit.modules.settings.Setting;

public class ToggleSetting extends Setting
{
    private Runnable runnable;
    public boolean enabled;

    public ToggleSetting(String name, boolean enabled)
    {
        super(name);
        this.enabled = enabled;
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
