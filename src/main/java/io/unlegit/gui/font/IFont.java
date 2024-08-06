package io.unlegit.gui.font;

import io.unlegit.gui.font.impl.FontRenderer;

public class IFont
{
    private static final String font = "assets/unlegit/font.ttf";
    public static final FontRenderer NORMAL = FontRenderer.create(font, 10);
}
