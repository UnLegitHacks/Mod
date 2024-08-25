package io.unlegit.modules.impl.misc;

import java.util.ArrayList;

import io.unlegit.events.impl.client.MessageE;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.impl.ModeSetting;
import io.unlegit.modules.settings.impl.ToggleSetting;
import net.minecraft.ChatFormatting;

@IModule(name = "Game Play", description = "Manages your experience for you.")
public class GamePlay extends ModuleU
{
    public ToggleSetting autoL = new ToggleSetting("Auto L", "Automatically says L when you kill a player.", false);
    public ModeSetting autoLMode = new ModeSetting("Auto L Mode", "The mode for Auto L.", new String[]
    {
        "Sweaty", "UnLeague"
    });
    
    private String[] sweaty = new String[]
    {
        "get gud",
        "lol",
        "imagine dying",
        "xD",
        "L",
        "noob",
        "ez",
        "learn how2 play",
    };
    
    private String[] unLeague = new String[]
    {
        "UnLegit users belike: Hit or miss I guess I never miss!",
        "UnLegitHacks . github. io is your new home",
        "UnLegit utility client no hax 100%",
        "Want some skills? Check out UnLegitHacks . github. io!",
        "No hax just beta testing the anti-cheat with UnLegit.",
        "Search UnLegitHacks . github. io to get the best mineman skills!",
        "Mama once told me, use UnLegit it's free",
        "UnLegit made this world a better place, killing you with it even more",
        "Why UnLegit? Cause it is the additon of pure skill and incredible intellectual abilities",
        "Wow! My combo is UnLegit'n!",
        "UnLegit. Among the only clients run by speakers of Breton",
        "Behind every UnLegit user, is an incredibly cool human being. Trust me.",
        "Quick Quiz: I am the opposite of Legit, what am I? UnLegit",
        "I have a good UnLegit config, don't blame me",
        "What should I choose? UnLegit or UnLegit?",
        "Do like Tenebrous, subscribe to DanTDM!",
        "don't use UnLegit? ok boomer",
        "What? You've never downloaded UnLegit 3.0? You know it's the best right?",
        "I don't hack I just UnLegit",
        "You have been offed by UnLegit oof oof",
        "I don't hack I just have the brand new UnLegit Gaming Chair",
        "UnLegit will help you! Oops, I killed you instead.",
        "Technoblade never dies",
        "My whole life changed since I discovered UnLegit",
        "I am a un-magician, thats how I am able to do all thos block game tricks",
        "Stop Hackusation me cuz im just UnLegit",
        "UnLegit helps reducing arm fatigue. Available for free at your local pharmacy.",
        "Stop it, get some help! Get UnLegit",
        "Look a divinity! He definitely uses UnLegit!",
        "In need of a cute present for Christmas? UnLegit is all you need!"
    };
    
    private ArrayList<String> alreadySaidMessages = new ArrayList<>();
    
    // Should work with most English servers
    public void onMessageReceive(MessageE e)
    {
        String message = ChatFormatting.stripFormatting(e.message.getString().replaceAll("\r", "\\\\r").replaceAll("\n", "\\\\n"));
        
        if (!mc.hasSingleplayerServer() && message.contains(" by " + mc.getUser().getName()))
        {
            String msgToSend = getMessage();
            mc.getConnection().sendChat(msgToSend);
        }
    }
    
    public String getMessage()
    {
        String message = null;
        
        if (autoLMode.equals("UnLeague"))
        {
            int length = unLeague.length;
            
            for (int i = 0; i < length * 3; i++)
            {
                String temporary = unLeague[(int) (Math.random() * length)];
                
                if (!alreadySaidMessages.contains(temporary))
                {
                    message = temporary;
                    alreadySaidMessages.add(message);
                    break;
                }
            }
            
            if (message == null)
            {
                alreadySaidMessages.clear();
                message = unLeague[(int) (Math.random() * length)];
            }
        }
        
        else
        {
            int length = sweaty.length;
            
            for (int i = 0; i < length * 3; i++)
            {
                String temporary = sweaty[(int) (Math.random() * length)];
                
                if (!alreadySaidMessages.contains(temporary))
                {
                    message = temporary;
                    alreadySaidMessages.add(message);
                    break;
                }
            }
            
            if (message == null)
            {
                alreadySaidMessages.clear();
                message = sweaty[(int) (Math.random() * length)];
            }
        }
        
        return message;
    }
}