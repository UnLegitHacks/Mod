package com.xxmicloxx.NoteBlockAPI.utils;

import java.util.ArrayList;

import com.xxmicloxx.NoteBlockAPI.model.CustomInstrument;

/**
 * Fields/methods for reflection &amp; version checking
 */
public class CompatibilityUtils {
	/**
	 * Return list of instuments which were added in specified version
	 * @param serverVersion 1.12 = 0.0112f, 1.14 = 0.0114f,...
	 * @return list of custom instruments, if no instuments were added in specified version returns empty list
	 */
	public static ArrayList<CustomInstrument> getVersionCustomInstruments(float serverVersion){
		ArrayList<CustomInstrument> instruments = new ArrayList<>();
		if (serverVersion == 0.0112f){
			instruments.add(new CustomInstrument((byte) 0, "Guitar", "block.note_block.guitar.ogg"));
			instruments.add(new CustomInstrument((byte) 0, "Flute", "block.note_block.flute.ogg"));
			instruments.add(new CustomInstrument((byte) 0, "Bell", "block.note_block.bell.ogg"));
			instruments.add(new CustomInstrument((byte) 0, "Chime", "block.note_block.icechime.ogg"));
			instruments.add(new CustomInstrument((byte) 0, "Xylophone", "block.note_block.xylobone.ogg"));
			return instruments;
		}

		if (serverVersion == 0.0114f){
			instruments.add(new CustomInstrument((byte) 0, "Iron Xylophone", "block.note_block.iron_xylophone.ogg"));
			instruments.add(new CustomInstrument((byte) 0, "Cow Bell", "block.note_block.cow_bell.ogg"));
			instruments.add(new CustomInstrument((byte) 0, "Didgeridoo", "block.note_block.didgeridoo.ogg"));
			instruments.add(new CustomInstrument((byte) 0, "Bit", "block.note_block.bit.ogg"));
			instruments.add(new CustomInstrument((byte) 0, "Banjo", "block.note_block.banjo.ogg"));
			instruments.add(new CustomInstrument((byte) 0, "Pling", "block.note_block.pling.ogg"));
			return instruments;
		}
		return instruments;
	}

	/**
	 * Return list of custom instruments based on song first custom instrument index and server version
	 * @param firstCustomInstrumentIndex
	 * @return
	 */
	public static ArrayList<CustomInstrument> getVersionCustomInstrumentsForSong(int firstCustomInstrumentIndex){
		ArrayList<CustomInstrument> instruments = new ArrayList<>();

        if (firstCustomInstrumentIndex == 16){
            instruments.addAll(getVersionCustomInstruments(0.0114f));
        }
		return instruments;
	}

}
