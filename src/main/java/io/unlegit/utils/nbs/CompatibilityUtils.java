package io.unlegit.utils.nbs;

import io.unlegit.utils.nbs.model.CustomInstrument;

import java.util.ArrayList;

/**
 * Fields/methods for reflection & version checking
 */
public class CompatibilityUtils
{
    /**
     * Returns a list of instuments which were added in specified version
     * @param serverVersion 1.12 = 0.0112F, 1.14 = 0.0114F,...
     */
    public static ArrayList<CustomInstrument> getVersionCustomInstruments(float serverVersion)
    {
        ArrayList<CustomInstrument> instruments = new ArrayList<>();
        byte zero = (byte) 0;

        if (serverVersion == 0.0112F)
        {
            instruments.add(new CustomInstrument(zero, "Guitar", "block.note_block.guitar.ogg"));
            instruments.add(new CustomInstrument(zero, "Flute", "block.note_block.flute.ogg"));
            instruments.add(new CustomInstrument(zero, "Bell", "block.note_block.bell.ogg"));
            instruments.add(new CustomInstrument(zero, "Chime", "block.note_block.icechime.ogg"));
            instruments.add(new CustomInstrument(zero, "Xylophone", "block.note_block.xylobone.ogg"));
        }

		else if (serverVersion == 0.0114F)
        {
            instruments.add(new CustomInstrument(zero, "Iron Xylophone", "block.note_block.iron_xylophone.ogg"));
            instruments.add(new CustomInstrument(zero, "Cow Bell", "block.note_block.cow_bell.ogg"));
            instruments.add(new CustomInstrument(zero, "Didgeridoo", "block.note_block.didgeridoo.ogg"));
            instruments.add(new CustomInstrument(zero, "Bit", "block.note_block.bit.ogg"));
            instruments.add(new CustomInstrument(zero, "Banjo", "block.note_block.banjo.ogg"));
            instruments.add(new CustomInstrument(zero, "Pling", "block.note_block.pling.ogg"));
        }

        return instruments;
    }

    /**
     * Return a list of custom instruments based on the song's
     * first custom instrument index & the server version
     */
    public static ArrayList<CustomInstrument> getVersionCustomInstrumentsForSong(int firstCustomInstrumentIndex)
    {
        ArrayList<CustomInstrument> instruments = new ArrayList<>();

        if (firstCustomInstrumentIndex == 16)
            instruments.addAll(getVersionCustomInstruments(0.0114F));

        return instruments;
    }
}
