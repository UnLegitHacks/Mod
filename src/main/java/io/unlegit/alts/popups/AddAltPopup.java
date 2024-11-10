package io.unlegit.alts.popups;

import io.unlegit.interfaces.IGui;
import io.unlegit.utils.render.Colorer;
import net.minecraft.client.gui.GuiGraphics;

public class AddAltPopup extends AltPopup implements IGui
{
    public AddAltPopup(int width, int height)
    {
        super(width, height);
    }

    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks)
    {
        int w2 = width / 2, h2 = height / 2;
        graphics.fill(w2 - 125, h2 - 75, w2 + 125, h2 + 752, Colorer.RGB(0, 0, 0, 100));
    }

//    public void onClose()
//    {
//    }
}
