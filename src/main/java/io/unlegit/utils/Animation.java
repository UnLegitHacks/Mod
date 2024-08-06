package io.unlegit.utils;

public class Animation
{
    private long time = System.currentTimeMillis();
    public boolean reverse = false;
    private int interval = 0;
    private float value = 0;
    
    public int wrap(int input)
    {
        value = (System.currentTimeMillis() - time) / (float) interval;
        value = value > 1 ? 1 : value;
        value = reverse ? (interval - (interval * value)) / (float) interval : value;
        return (int) (input * value);
    }
    
    public boolean finished() { return value == 1; }
    public Animation(int interval) { this.interval = interval; }
}