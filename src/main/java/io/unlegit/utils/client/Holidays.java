package io.unlegit.utils.client;

import java.util.Calendar;
import java.util.Date;

public class Holidays
{
    private static String holiday = "";
    
    static
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        
        int month = calendar.get(Calendar.MONTH), day = calendar.get(Calendar.DAY_OF_MONTH) + 1;
        
        if (month == Calendar.DECEMBER && day >= 25)
            holiday = "Merry Christmas!";
        else if (month == Calendar.JANUARY && day == 1)
            holiday = "Happy New Year!";
        else if (month == Calendar.OCTOBER && day >= 29)
            holiday = "OOoooOOOoooo! Spooky!";
    }
    
    public static String get() { return holiday; }
    public static boolean todayOne() { return !holiday.isEmpty(); }
}
