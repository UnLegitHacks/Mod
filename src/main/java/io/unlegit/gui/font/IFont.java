package io.unlegit.gui.font;

import io.unlegit.UnLegit;
import io.unlegit.gui.font.impl.FontRenderer;

public class IFont
{
    private static final String font = "assets/unlegit/font.ttf";
    public static FontRenderer NORMAL, MEDIUM, LARGE;
    
    public static void init()
    {
        if ("Fancy".equals(UnLegit.THEME))
        {
            NORMAL = FontRenderer.fancy(font, 10);
            MEDIUM = FontRenderer.fancy(font, 15);
            LARGE = FontRenderer.fancy(font, 20);
        }
        
        else
        {
            NORMAL = FontRenderer.vanilla(10);
            MEDIUM = FontRenderer.vanilla(15);
            LARGE = FontRenderer.vanilla(20);
        }
    }
}
