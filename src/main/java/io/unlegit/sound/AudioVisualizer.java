package io.unlegit.sound;

import io.unlegit.gui.clickgui.ClickGui;
import io.unlegit.interfaces.IGui;
import io.unlegit.interfaces.IMinecraft;
import net.minecraft.client.gui.GuiGraphics;

public class AudioVisualizer implements IMinecraft, IGui
{
    private static AudioVisualizer instance = new AudioVisualizer();
    
    public void draw(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks)
    {
        int width = mc.getWindow().getGuiScaledWidth(), height = mc.getWindow().getGuiScaledHeight();
        float offset = 0;
        
        for (int i = 0; i < 256; i++)
        {
            horzGradient(graphics, (int) offset, height - Math.abs(SpeakerConf.buffers[i]) / 2, (int) (offset + (width / 256F)), height, ClickGui.get().brightBluple(width - (int) offset, 128), ClickGui.get().brightBluple(width - (int) (offset + (width / 256F)), 128));
            offset += width / 256F;
        }
    }
    
    public static AudioVisualizer get() { return instance; }
}
