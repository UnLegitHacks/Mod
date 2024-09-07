package io.unlegit.utils.client;

public class ConvTime
{
    public static long millis()
    {
        return System.nanoTime() / 1000000;
    }
}
