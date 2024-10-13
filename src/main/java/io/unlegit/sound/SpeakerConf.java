package io.unlegit.sound;

import javax.sound.sampled.*;

import io.unlegit.UnLegit;
import io.unlegit.interfaces.IMinecraft;

public class SpeakerConf implements IMinecraft
{
    public static volatile int[] buffers = new int[256];
    public static boolean supported = true;
    public static DataLine.Info info;
    
    static
    {
        Mixer mixer = null;
        
        for (Mixer.Info info : AudioSystem.getMixerInfo())
        {
            if (info.getName().contains("Stereo Mix") || info.getName().contains("What U Hear"))
                mixer = AudioSystem.getMixer(info);
        }
        
        try
        {
            if (mixer == null)
            {
                supported = false;
                throw new NullPointerException();
            }
            
            AudioFormat format = new AudioFormat(44100, 16, 2, true, true);
            info = new DataLine.Info(TargetDataLine.class, format);
            byte[] buffer = new byte[4096];
            
            TargetDataLine line = (TargetDataLine) mixer.getLine(info);
            line.open(format);
            line.start();
            
            Thread listener = new Thread(() ->
            {
                while (true)
                {
                    int bytesRead = line.read(buffer, 0, buffer.length);
                    
                    if (bytesRead > 0)
                    {
                        for (int i = 0; i < 256; i++)
                            buffers[i] = buffer[i];
                    }
                }
            });
            
            listener.setDaemon(true);
            listener.start();
        }
        
        catch (Exception e)
        {
            UnLegit.LOGGER.info("Audio visualization is not supported.");
        }
    }
    
    public static void load() {}
}
