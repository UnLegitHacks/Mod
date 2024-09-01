package io.unlegit.utils;

public class ReflectionUtil
{
    public static boolean classExists(String name)
    {
        try
        {
            return Class.forName(name) != null;
        } catch (Throwable t) { return false; }
    }
}
