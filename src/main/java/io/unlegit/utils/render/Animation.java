package io.unlegit.utils.render;

import io.unlegit.UnLegit;

public class Animation
{
    private long time = System.currentTimeMillis();
    public boolean reverse = false;
    public int interval = 0;
    private float value = 0;
    
    public int wrap(int input)
    {
        update(); return (int) (input * value);
    }
    
    public float get()
    {
        update(); return value;
    }
    
    private void update()
    {
        value = (System.currentTimeMillis() - time) / (float) interval;
        value = value > 1 ? 1 : value;
        value = reverse ? (interval - (interval * value)) / (float) interval : value;
    }
    
    public boolean finished() { update(); return value == (reverse ? 0 : 1); }
    
    public Animation(int interval)
    {
        if ("Vanilla".equals(UnLegit.THEME)) interval = 1;
        this.interval = interval;
    }
}