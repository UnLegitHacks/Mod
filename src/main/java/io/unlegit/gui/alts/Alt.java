package io.unlegit.gui.alts;

import java.util.UUID;

public class Alt
{
    public String name, accessToken;
    public UUID uuid;
    
    public boolean premium()
    {
        return uuid != null;
    }
    
    public Alt(String name) { this.name = name; }
    
    public Alt(String name, UUID uuid, String accessToken)
    {
        this.name = name; this.uuid = uuid; this.accessToken = accessToken;
    }
}