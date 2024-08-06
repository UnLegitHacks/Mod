package io.unlegit.events;

import static io.unlegit.events.EventHandler.handleEvent;

import java.util.ArrayList;

/**
 * Google's EventBus is kind of slow,
 * so UnLegit uses its own.
 */
public class EventBus
{
    private ArrayList<EventListener> listeners = new ArrayList<>();
    
    public void register(EventListener listener)
    {
        if (!listeners.contains(listener)) listeners.add(listener);
    }
    
    public void unregister(EventListener listener)
    {
        if (listeners.contains(listener)) listeners.remove(listener);
    }
    
    public void post(Event e)
    {
        for (int i = 0; i < listeners.size(); i++)
        {
            EventListener listener = listeners.get(i);
            handleEvent(listener, e);
        }
    }
}