package io.unlegit.gui.font;

import io.unlegit.gui.font.impl.FontRenderer;

public class IFont
{
    public static void init() {}
    private static final String font = "assets/unlegit/font.ttf";
    public static final FontRenderer NORMAL = FontRenderer.create(font, 10),
            MEDIUM = FontRenderer.create(font, 15), LARGE = FontRenderer.create(font, 20);
}
