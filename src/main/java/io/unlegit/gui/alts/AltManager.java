package io.unlegit.gui.alts;

import java.util.ArrayList;

import io.unlegit.utils.render.Animation;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class AltManager extends Screen
{
    private static AltManager INSTANCE = new AltManager();
    private ArrayList<Alt> alts = new ArrayList<>();
    public Animation animation = null;
    
    protected AltManager()
    {
        super(Component.literal("Alt Manager"));
    }
    
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks)
    {
        super.render(graphics, mouseX, mouseY, partialTicks);
    }
    
    public ArrayList<Alt> alts() { return alts; }
    public static AltManager get() { return INSTANCE; }
}
