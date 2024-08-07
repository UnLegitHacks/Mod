package io.unlegit.gui;

import java.awt.Color;
import java.util.ArrayList;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.realmsclient.RealmsMainScreen;

import io.unlegit.gui.buttons.UnButton;
import io.unlegit.gui.font.IFont;
import io.unlegit.interfaces.IGui;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.CreditsAndAttributionScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.SafetyScreen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * If any mods add buttons to the main menu,
 * unfortunately this screen won't have it.
 * However this looks aesthetically better than
 * the vanilla main menu, so it's worth it.
 */
public class UnTitleScreen extends Screen implements IGui
{
    private static final Component COPYRIGHT_TEXT = Component.translatable("title.credits");
    private ArrayList<UnButton> buttons = new ArrayList<>();
    private ResourceLocation logo = null;
    
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
    {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderPanorama(guiGraphics, partialTicks);
        GlStateManager._enableBlend();
        guiGraphics.setColor(1, 1, 1, 1);
        
        for (UnButton button : buttons)
            button.render(guiGraphics, mouseX, mouseY);
        
        String titleText = "Minecraft " + SharedConstants.getCurrentVersion().getName();
        
        if (minecraft.isDemo()) titleText += " Demo";
        else titleText += ("release".equalsIgnoreCase(minecraft.getVersionType()) ? "" : "/" + minecraft.getVersionType());
        if (Minecraft.checkModStatus().shouldReportAsModified()) { titleText += I18n.get("menu.modded"); }
        
        IFont.NORMAL.drawStringWithShadow(guiGraphics, titleText, 0, height - 13, Color.WHITE);
        IFont.NORMAL.drawStringWithShadow(guiGraphics, COPYRIGHT_TEXT.getString(), width - IFont.NORMAL.getStringWidth(COPYRIGHT_TEXT.getString()) - 1, height - 13, Color.WHITE);
        guiGraphics.blit(logo, (width / 2) - 96, (height / 2) - 60, 192, 60, 192, 60, 192, 60);
        
        if (mouseOver((int) mouseX, (int) mouseY, width - IFont.NORMAL.getStringWidth(COPYRIGHT_TEXT.getString()) - 1, height - 13, width, height))
            guiGraphics.fill(width - IFont.NORMAL.getStringWidth(COPYRIGHT_TEXT.getString()) - 1, height - 2, width, height - 1, -1);
    }
    
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        if (mouseOver((int) mouseX, (int) mouseY, width - IFont.NORMAL.getStringWidth(COPYRIGHT_TEXT.getString()) - 1, height - 13, width, height))
            minecraft.setScreen(new CreditsAndAttributionScreen(this));
        
        for (UnButton buttonComponent : buttons)
            buttonComponent.mouseClicked(mouseX, mouseY, button);
        
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    protected void init()
    {
        int numOfButtons = 5, x = (width / 2) - (32 * numOfButtons),
                y = (height / 2) + 20;
        buttons.clear();
        buttons.add(new UnButton(Component.translatable("menu.singleplayer").getString(), "singleplayer", x, y, () -> minecraft.setScreen(new SelectWorldScreen(this))));
        
        buttons.add(new UnButton(Component.translatable("menu.multiplayer").getString(), "multiplayer", x += 64, y, () ->
        {
            Screen screen = minecraft.options.skipMultiplayerWarning ? new JoinMultiplayerScreen(this) : new SafetyScreen(this);
            minecraft.setScreen(screen);
        }));
        
        buttons.add(new UnButton(Component.translatable("menu.online").getString(), "realms", x += 64, y, () -> minecraft.setScreen(new RealmsMainScreen(this))));
        buttons.add(new UnButton(Component.translatable("menu.options").getString(), "settings", x += 64, y, () -> minecraft.setScreen(new OptionsScreen(this, minecraft.options))));
        buttons.add(new UnButton("Alt Manager", "altmanager", x += 64, y, () -> {}));
        logo = withLinearScaling(ResourceLocation.fromNamespaceAndPath("unlegit", "main_menu/unlegit.png"));
    }
    
    public void onClose() {}
    public UnTitleScreen() { super(Component.translatable("narrator.screen.title")); }
}
