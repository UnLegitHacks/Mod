package io.unlegit.utils;

import io.unlegit.UnLegit;
import io.unlegit.interfaces.IMinecraft;
import io.unlegit.utils.io.FileUtil;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvents;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class SoundUtil implements IMinecraft
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
            } catch (Exception e) {
                UnLegit.LOGGER.error("Sound file {} failed to load.", file.getName()); }
        }
        
        // If multiple modules are turned on at once, don't play several sounds at once
        if (System.currentTimeMillis() - time < 100) return;
        
        new Thread(() ->
        {
            try (InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                 AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream))
            {
                Clip clip = AudioSystem.getClip();
                clip.addLineListener(e -> { if (e.getType() == LineEvent.Type.STOP) clip.close(); });
                clip.open(audioInputStream);
                clip.start();
            } catch (Exception e) {}
        }).start();
        
        time = System.currentTimeMillis();
    }
    
    public static void playEnableSound()
    {
        if ("Fancy".equals(UnLegit.THEME))
            playSound("enable.wav");
        else
        {
            SoundManager sound = mc.getSoundManager();
            
            if (System.currentTimeMillis() - time >= 100)
                sound.play(SimpleSoundInstance.forUI(SoundEvents.BAMBOO_WOOD_PRESSURE_PLATE_CLICK_ON, 1, 1));
            
            time = System.currentTimeMillis();
        }
    }
    
    public static void playDisableSound()
    {
        if ("Fancy".equals(UnLegit.THEME))
            playSound("disable.wav");
        else
        {
            SoundManager sound = mc.getSoundManager();
            
            if (System.currentTimeMillis() - time >= 100)
                sound.play(SimpleSoundInstance.forUI(SoundEvents.BAMBOO_WOOD_PRESSURE_PLATE_CLICK_OFF, 1, 1));
            
            time = System.currentTimeMillis();
        }
    }
    
    public static void playActionSound() { playSound("action.wav"); }
}