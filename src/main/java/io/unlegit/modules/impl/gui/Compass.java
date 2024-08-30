package io.unlegit.modules.impl.gui;

import java.awt.Color;
import java.util.ArrayList;

import io.unlegit.events.impl.render.GuiRenderE;
import io.unlegit.gui.font.IFont;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import net.minecraft.client.gui.GuiGraphics;

@IModule(name = "Compass", description = "A simplistic compass that shows directions.")
public class Compass extends ModuleU
{
    private ArrayList<String> cardinals = new ArrayList<>(),
            ordinals = new ArrayList<>(),
            numbers = new ArrayList<>();
    
    public Compass()
    {
        add(cardinals, "S", "E", "N", "W");
        add(ordinals, "SE", "NE", "NW", "SW");
    }
    
    public void onGuiRender(GuiRenderE e)
    {
        float rotation = (mc.player.getYRot() % 360F) / 360F;
        int width = mc.getWindow().getGuiScaledWidth();
        GuiGraphics graphics = e.graphics;
        int offset = 0;
        
        for (String direction : cardinals)
        {
            int x = 0;
            IFont.LARGE.drawCenteredString(graphics, direction, x = (offset + -640 + (int) ((width / 2) + (rotation * 640))), 5, getColor(width, x));
            IFont.LARGE.drawCenteredString(graphics, direction, x = (offset + (int) ((width / 2) + (rotation * 640))), 5, getColor(width, x));
            IFont.LARGE.drawCenteredString(graphics, direction, x = (offset + 640 + (int) ((width / 2) + (rotation * 640))), 5, getColor(width, x));
            offset += 160;
        }
        
        offset = 80;
        
        for (String direction : ordinals)
        {
            int x = 0;
            IFont.MEDIUM.drawCenteredString(graphics, direction, x = (offset + -640 + (int) ((width / 2) + (rotation * 640))), 8, getColor(width, x));
            IFont.MEDIUM.drawCenteredString(graphics, direction, x = (offset + (int) ((width / 2) + (rotation * 640))), 8, getColor(width, x));
            IFont.MEDIUM.drawCenteredString(graphics, direction, x = (offset + 640 + (int) ((width / 2) + (rotation * 640))), 8, getColor(width, x));
            offset += 160;
        }
    }
    
    public void add(ArrayList<String> arrayList, String... degrees)
    {
        for (String degree : degrees)
            arrayList.add(degree);
    }
    
    public Color getColor(int width, float x)
    {
        float alpha = 255 - Math.abs((width / 2) - x) * 2;
        if (alpha < 0) alpha = 0; if (alpha > 255) alpha = 255;
        return new Color(255, 255, 255, (int) alpha);
    }
}
