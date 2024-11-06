package io.unlegit.utils.nbs.model;

/**
 * Holds a custom instrument from a sound file
 */
public record CustomInstrument(byte index, String name, String soundFileName)
{
    public CustomInstrument(byte index, String name, String soundFileName)
    {
        this.index = index;
        this.name = name;
        this.soundFileName = soundFileName.replaceAll(".ogg", "");
    }
}
