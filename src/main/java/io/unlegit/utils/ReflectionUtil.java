package io.unlegit.utils;

public class ReflectionUtil
{
    public static boolean classExists(String name)
    {
        try
        {
            Class.forName(name);
            return true;
        } catch (Throwable t) { return false; }
    }
}
