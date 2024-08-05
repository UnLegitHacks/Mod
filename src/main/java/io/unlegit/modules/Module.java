package io.unlegit.modules;

import java.util.Locale.Category;

import io.unlegit.UnLegit;
import io.unlegit.events.EventListener;
import io.unlegit.interfaces.IMinecraft;
import io.unlegit.interfaces.IModule;
import io.unlegit.utils.SoundUtil;

public class Module implements IMinecraft, EventListener
{
    private IModule iModule = getClass().getAnnotation(IModule.class);
    public String name = iModule.name(), description = iModule.description();
    public Category category = getCategory();
    public boolean shouldNotOnStart = false;
    private boolean enabled = false;
    public int keyBind = 0;
    
    public void onEnable()
    {
        enabled = true;
        UnLegit.eventBus.register(this);
    }
    
    public void onDisable()
    {
        enabled = false;
        UnLegit.eventBus.unregister(this);
    }
    
    public void toggle()
    {
        setEnabled(!isEnabled());
        if (enabled) SoundUtil.playEnableSound(); else SoundUtil.playDisableSound();
    }
    
    public boolean isEnabled()
    {
        return enabled;
    }
    
    public void setEnabled(boolean enabled)
    {
        if (enabled && !this.enabled) onEnable();
        else if (!enabled && this.enabled) onDisable();
    }
    
    /**
     * Gets the category from the package the module is in
     */
    private Category getCategory()
    {
        String packageName = getClass().getName();
        packageName = packageName.substring(21, packageName.length());
        packageName = packageName.substring(0, packageName.indexOf("."));
        return Category.valueOf(packageName.toUpperCase());
    }
}
