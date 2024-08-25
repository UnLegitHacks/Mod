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
    private char[] randomLetters = " ,'.".toCharArray();
    private ElapTime elapTime = new ElapTime();
    
    public void onUpdate()
    {
        if (elapTime.passed((long) delay.value))
        {
            StringBuilder message = new StringBuilder(this.message.text);
            
            if (bypassRestriction.enabled)
            {
                for (int i = 0; i < message.length(); i++)
                {
                    if (Math.random() >= 0.15D) continue;
                    message.insert(i, randomLetters[(int) (Math.random() * randomLetters.length)]);
                }
                
                message.append(" " + (int) (Math.random() * 100));
            }
            
            mc.getConnection().sendChat(message.toString());
        }
    }
}
