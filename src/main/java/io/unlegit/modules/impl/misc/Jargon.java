package io.unlegit.modules.impl.misc;

import org.apache.commons.lang3.StringUtils;

import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;

@IModule(name = "Jargon", description = "Sends a random excuse in chat.")
public class Jargon extends ModuleU
{
    public void onEnable()
    {
        super.onEnable();
        mc.getConnection().sendChat(constructMsg());
        toggle();
    }
    
    public String constructMsg()
    {
        String[] errorMessages = new String[] {"invalid supported rates 99", "fast termomixer",
                "ultradurable z345 turbo 12", "hardware accelerated EQ", "speedball AQ345 generator",
                "XCD k45 electric turbo", "RF hypersonic", "giant Q23", "giant racing Z9", "swift",
                "double turbocharged", "supersonic SL234", "turbospeed 930", "turbo highspeed GL4", "GL3 grfx turbo",
                "E24 focus sloter", "super overclocked", "overpowered", "983k", "99 full comfort", "GTX 1080 adv",
                "full RAZOR", "hot DPI 2000", "90GB fusion", "feedback 3D", "MX freeze", "v1999 golden", "giant RATIO",
                "legendary", "awesome", "brilliant", "advanced AI"},
                
                 objects = new String[] {"gaming carpet", "studio", "windows 95 os", "HDMI cable", "potassium", "SSD",
                "PCI", "PCI express", "overclock", "super pc", "jitter clicker mouse", "mouse", "keyboard",
                "gaming chair", "screen", "display", "16.9 ratio display", "touchpad", "spacebar", "webcam", "desk",
                "headphones", "earbuds", "speakers", "power adapter", "processor", "graphics card", "DVD drive", "fans",
                "usb port", "thunderbolt port", "virtual reality headset", "google glass", "backlight", "computer",
                "laptop", "sd card slot", "escape key", "function keys"},
                 
                 modifiers = new String[] {"maybe", "just", "for sure", "simply", "possibly", "without any doubts"},
                 possessions = new String[] {"pro", "replica", "I made", "I bought {4}", "I found {4}"},
                 locations = new String[] {"on ebay", "on amazon", "on craigslist", "on the internet", "online",
                "on the TOR network", "in a garage", "on the deep web", "near the grocery shop"};
        
        return generate("I don't hack it's {2} my {0} {1} {3}", errorMessages, objects, modifiers, possessions, locations);
    }
    
    private String generate(String base, String[]... words)
    {
        for (int i = 0; i < words.length; i++)
        {
            String word = random(words[i]);
            base = StringUtils.replace(base, "{" + i + "}", word);
        }
        
        return base;
    }
    
    private String random(String[] words)
    {
        return words[(int) (Math.random() * words.length)];
    }
}
