package io.unlegit.events;

import static io.unlegit.events.EventHandler.handleEvent;

import java.util.ArrayList;

/**
 * Google's EventBus is kind of slow,
 * so UnLegit uses its own.
 */
public class EventBus
{
    private ArrayList<EventListener> registeredListeners = new ArrayList<>();
    
    public void register(EventListener listener)
    {
        registeredListeners.add(listener);
    }
    
    public void unregister(EventListener listener)
    {
        registeredListeners.remove(listener);
    }
    
    public void post(Event e)
    {
        for (int i = 0; i < registeredListeners.size(); i++)
        {
            EventListener listener = registeredListeners.get(i);
            handleEvent(listener, e);
        }
    }
}