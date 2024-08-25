package io.unlegit.events.impl.client;

import io.unlegit.events.Event;
import net.minecraft.network.chat.Component;

public class MessageE implements Event
{
    private static MessageE e = new MessageE();
    public Component message;
    
    public static MessageE get(Component message)
    {
        e.message = message;
        return e;
    }
}
