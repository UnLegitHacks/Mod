package io.unlegit.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.realmsclient.RealmsMainScreen;
import io.unlegit.alts.AltManager;
import io.unlegit.gui.buttons.UnButton;
import io.unlegit.gui.buttons.UnPlainTextButton;
import io.unlegit.gui.buttons.UnPlainTextButton.UnStyle;
import io.unlegit.gui.font.IFont;
import io.unlegit.interfaces.IGui;
import io.unlegit.utils.client.Holidays;
import io.unlegit.utils.render.Colorer;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.CreditsAndAttributionScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.SafetyScreen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;

/**
 * Looks aesthetically better
 * than the vanilla main menu.
 */
public class UnTitleScreen extends Screen implements IGui
{
    private static final Component COPYRIGHT_TEXT = Component.translatable("title.credits");
    private final ArrayList<UnButton> buttons = new ArrayList<>();
    private UnPlainTextButton copyrightButton, exitButton;
    private ResourceLocation logo = null;
    
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks)
    {
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderPanorama(graphics, partialTicks);
        GlStateManager._enableBlend();
        
        for (UnButton button : buttons)
            button.render(graphics, mouseX, mouseY);
        
        String titleText = "Minecraft " + SharedConstants.getCurrentVersion().getName(),
               accountText = "Logged into " + minecraft.getUser().getName();
        
        if (minecraft.isDemo()) titleText += " Demo";
        else titleText += ("release".equalsIgnoreCase(minecraft.getVersionType()) ? "" : "/" + minecraft.getVersionType());
        if (Minecraft.checkModStatus().shouldReportAsModified()) { titleText += I18n.get("menu.modded"); }
        
        IFont.NORMAL.drawStringWithShadow(graphics, titleText, 2, height - 14, -1);
        IFont.NORMAL.drawStringWithShadow(graphics, accountText, width - IFont.NORMAL.getStringWidth(accountText) - 3, 2, Colorer.RGB(200, 200, 200, 200));
        graphics.blit(RenderType::guiTextured, logo, (width / 2) - 96, (height / 2) - 60, 192, 60, 192, 60, 192, 60);
        
        copyrightButton.render(graphics, mouseX, mouseY);
        exitButton.render(graphics, mouseX, mouseY);
    }
    
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        for (UnButton buttonComponent : buttons)
            buttonComponent.mouseClicked(mouseX, mouseY, button);
        
        copyrightButton.mouseClicked(mouseX, mouseY, button);
        exitButton.mouseClicked(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    protected void repositionElements()
    {
        int numOfButtons = 5, x = (width / 2) - (32 * numOfButtons), y = (height / 2) + 20;
        Component quitText = Component.translatable("menu.quit");
        
        buttons.clear();
        buttons.add(new UnButton(Component.translatable("menu.singleplayer").getString(), "singleplayer", x, y, () -> minecraft.setScreen(new SelectWorldScreen(this))));
        
        buttons.add(new UnButton(Component.translatable("menu.multiplayer").getString(), "multiplayer", x += 64, y, () ->
        {
            Screen screen = minecraft.options.skipMultiplayerWarning ? new JoinMultiplayerScreen(this) : new SafetyScreen(this);
            minecraft.setScreen(screen);
        }));
        
        buttons.add(new UnButton(Component.translatable("menu.online").getString(), "realms", x += 64, y, () -> minecraft.setScreen(new RealmsMainScreen(this))));
        buttons.add(new UnButton(Component.translatable("menu.options").getString(), "settings", x += 64, y, () -> minecraft.setScreen(new OptionsScreen(this, minecraft.options))));
        buttons.add(new UnButton("Alt Manager", "altmanager", x + 64, y, () -> minecraft.setScreen(AltManager.get(this))));
        
        boolean holiday = Holidays.todayOne();
        String copyrightText = holiday ? Holidays.get() : COPYRIGHT_TEXT.getString();
        
        copyrightButton = new UnPlainTextButton(copyrightText,
                width - (holiday ? IFont.MEDIUM : IFont.NORMAL).getStringWidth(copyrightText) - 2,
                height - (holiday ? 20 : 14), UnStyle.UNDERLINE, () -> minecraft.setScreen(
                        new CreditsAndAttributionScreen(this)));
        
        if (holiday) copyrightButton.special = true;
        
        exitButton = new UnPlainTextButton(quitText.getString(), 3, 2,
                UnStyle.FADE, () -> minecraft.stop());
        
        logo = get(ResourceLocation.fromNamespaceAndPath("unlegit", "mainmenu/unlegit.png"));
    }
    
    protected void init() { repositionElements(); }
    public void onClose() {}
    
    public UnTitleScreen() { super(Component.translatable("narrator.screen.title")); }
}
