package io.unlegit.events;

import io.unlegit.UnLegit;
import io.unlegit.events.impl.entity.MotionE;
import io.unlegit.modules.impl.misc.Rotations;

import java.util.ArrayList;

import static io.unlegit.events.EventHandler.handleEvent;

/**
 * Google's EventBus is kind of slow,
 * so UnLegit uses its own.
 */
public class EventBus
{
    private final ArrayList<EventListener> listeners = new ArrayList<>();
    
    public void register(EventListener listener)
    {
        if (!listeners.contains(listener)) listeners.add(listener);
    }
    
    public void unregister(EventListener listener)
    {
        listeners.remove(listener);
    }
    
    public void post(Event e)
    {
        for (int i = 0; i < listeners.size(); i++)
        {
            EventListener listener = listeners.get(i);
            handleEvent(listener, e);
        }
        
        if (e instanceof MotionE)
        {
            Rotations rotations = (Rotations) UnLegit.modules.get("Rotations");
            if (rotations.isEnabled()) rotations.onPostMotion((MotionE) e);
        }
    }
}