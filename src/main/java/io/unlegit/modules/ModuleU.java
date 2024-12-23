package io.unlegit.modules;

import io.unlegit.UnLegit;
import io.unlegit.events.EventListener;
import io.unlegit.interfaces.IMinecraft;
import io.unlegit.interfaces.IModule;
import io.unlegit.bots.AutoPlay;
import io.unlegit.utils.SoundUtil;

/** Module -> ModuleU to prevent clashing with java.lang.Module */
public class ModuleU implements IMinecraft, EventListener
{
    private final IModule iModule = getClass().getAnnotation(IModule.class);
    public String name = iModule.name(), description = iModule.description();
    public boolean noStart = false, hidden = false;
    public CategoryM category = getCategory();
    private boolean enabled = false;
    public int key = 0;
    
    public void onEnable()
    {
        enabled = true;
        UnLegit.events.register(this);
    }
    
    public void onDisable()
    {
        enabled = false;
        UnLegit.events.unregister(this);
    }
    
    public void toggle()
    {
        setEnabled(!isEnabled());
        if (enabled) SoundUtil.playEnableSound(); else SoundUtil.playDisableSound();
    }
    
    public boolean isEnabled() { return enabled; }
    
    public void setEnabled(boolean enabled)
    {
        if (enabled && !this.enabled) onEnable();
        else if (!enabled && this.enabled) onDisable();
    }
    
    // Used by modules to change their settings after the config has loaded
    public void settingsReload() {}
    
    /**
     * Gets the category from the package the module is in.
     * This may break and so you need to add exceptions
     * for certain modules, such as Auto Play.
     */
    private CategoryM getCategory()
    {
        if (this instanceof AutoPlay)
            return CategoryM.PLAYER;

        String packageName = getClass().getName();
        packageName = packageName.substring(24);
        packageName = packageName.substring(0, packageName.indexOf("."));
        return CategoryM.valueOf(packageName.toUpperCase());
    }
    
    public boolean renderCondition() { return enabled; }
}
