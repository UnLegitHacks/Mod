package io.unlegit.utils;

import java.io.*;

import javax.sound.sampled.*;

import io.unlegit.UnLegit;
import io.unlegit.utils.io.FileUtil;

public class SoundUtil
{
    private static long time = System.currentTimeMillis();
    
    public static void playSound(String path)
    {
        File file = new File("unlegit/sounds/" + path);
        
        if (!file.exists())
        {
            file.getParentFile().mkdirs();
            
            try (InputStream inputStream = SoundUtil.class.getClassLoader().getResourceAsStream("assets/unlegit/sounds/" + path))
            {
                FileUtil.extract(inputStream, file);
            } catch (Exception e) { UnLegit.LOGGER.error("Sound file " + file.getName() + " failed to load. :("); }
        }
        
        // If multiple modules are turned on at once, don't play several sounds at once
        if (System.currentTimeMillis() - time < 100) return;
        
        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
             AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream))
        {
            Clip clip = AudioSystem.getClip();
            clip.addLineListener(e -> { if (e.getType() == LineEvent.Type.STOP) clip.close(); });
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {}
        
        time = System.currentTimeMillis();
    }
    
    public static void playEnableSound() { playSound("enable.wav"); }
    public static void playDisableSound() { playSound("disable.wav"); }
    public static void playActionSound() { playSound("action.wav"); }
}