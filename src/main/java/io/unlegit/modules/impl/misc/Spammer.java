package io.unlegit.modules.impl.misc;

import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.impl.SliderSetting;
import io.unlegit.modules.settings.impl.TextSetting;
import io.unlegit.modules.settings.impl.ToggleSetting;
import io.unlegit.utils.ElapTime;

@IModule(name = "Spammer", description = "Spams messages in the chat.")
public class Spammer extends ModuleU
{
    public TextSetting message = new TextSetting("Message", "The message to spam.", "Hacking is bad for health guys!");
    public SliderSetting delay = new SliderSetting("Delay (ms)", "The delay.", 250, 5000, 10000);
    public ToggleSetting bypassRestriction = new ToggleSetting("Bypass Restriction", "Bypasses the \"You sent the same message twice!\".", true);
    private ElapTime elapTime = new ElapTime();
    
    public void onUpdate()
    {
        if (elapTime.passed((long) delay.value))
        {
            String message = this.message.text;
            // TODO: bypassRestriction
            mc.getConnection().sendChat(message);
        }
    }
}
