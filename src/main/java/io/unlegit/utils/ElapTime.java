package io.unlegit.utils;

import io.unlegit.utils.client.Holidays;

import java.util.Calendar;

public class ElapTime
{
    private long time = System.currentTimeMillis();
    
    public boolean passed(long ms)
    {
        boolean pass = (time + ms) <= System.currentTimeMillis();
        if (pass) reset();
        return pass;
    }
    
    public static int getHour()
    {
        return Holidays.calendar.get(Calendar.HOUR_OF_DAY);
    }
    
    private void reset() { time = System.currentTimeMillis(); }
}
