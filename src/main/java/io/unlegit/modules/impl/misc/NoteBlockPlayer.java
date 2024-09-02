package io.unlegit.modules.impl.misc;

import java.io.File;

import io.unlegit.UnLegit;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.impl.ModeSetting;
import io.unlegit.utils.io.NbsReader;
import io.unlegit.utils.io.NbsReader.NbsData;

@IModule(name = "Note Block Player", description = "Plays note blocks! Needs a 5x5 floor & NBS files.")
public class NoteBlockPlayer extends ModuleU
{
    public ModeSetting nbsFile = new ModeSetting("NBS File", "The reference for the player.", new String[] {"404 (none)"});
    public static final File nbsDirectory = new File("unlegit/nbs");
    private Thread playerThread;
    
    public void onEnable()
    {
        super.onEnable();
        
        if (mc.player == null)
        {
            toggle();
            return;
        }
        
        playerThread = Thread.ofVirtual().name("Note Block Player").start(() ->
        {
            NbsData nbsData = NbsReader.read(new File(nbsDirectory, nbsFile.selected));
            UnLegit.LOGGER.info(nbsData.tempo() + "");
        });
    }
    
    public void onDisable()
    {
        if (playerThread != null && playerThread.isAlive()) playerThread.interrupt();
        playerThread = null;
    }
    
    public void settingsReload()
    {
        if (!nbsDirectory.exists()) nbsDirectory.mkdirs();
        File[] nbsFiles = nbsDirectory.listFiles();
        
        if (nbsFiles != null && nbsFiles.length != 0)
        {
            String[] names = new String[nbsFiles.length];
            int i = 0;
            
            for (File nbs : nbsFiles)
            {
                names[i] = nbs.getName(); i++;
            }
            
            nbsFile.modes = names;
            nbsFile.selected = names[0];
        }
    }
}
