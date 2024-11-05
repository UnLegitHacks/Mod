package io.unlegit.bots;

import io.unlegit.UnLegit;
import io.unlegit.events.EventListener;
import io.unlegit.interfaces.IMinecraft;

public abstract class Bot implements IMinecraft, EventListener
{
    protected float CPS = 0;

    public void onEnable()
    {
        UnLegit.events.register(this);
    }

    public void onDisable()
    {
        UnLegit.events.unregister(this);
    }

    protected float updateCPS()
    {
        return (float) (6 + (6 * Math.random()));
    }
}
