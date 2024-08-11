package io.unlegit.modules.settings;

public class Setting
{
    public String name, description;
    public boolean hidden;
    
    protected Setting(String name, String description)
    {
        this.name = name; this.description = description;
    }
}
