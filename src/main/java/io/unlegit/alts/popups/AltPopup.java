package io.unlegit.alts.popups;

import net.minecraft.client.gui.GuiGraphics;

public abstract class AltPopup
{
    protected int width, height;

    public AltPopup(int width, int height)
    {
        this.width = width;
        this.height = height;
    }

    public abstract void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks);

    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        return false;
    }

    public void onClose()
    {
    }
}
