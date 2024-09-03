package com.xxmicloxx.NoteBlockAPI.model;

/**
 * Create custom instruments from a sound file
 * 
 */
public class CustomInstrument {
	
	private byte index;
	private String name;
	private String soundFileName;

	/**
	 * Creates a CustomInstrument
	 * @param index
	 * @param name
	 * @param soundFileName
	 */
	public CustomInstrument(byte index, String name, String soundFileName) {
		this.index = index;
		this.name = name;
		this.soundFileName = soundFileName.replaceAll(".ogg", "");
	}

	/**
	 * Gets index of CustomInstrument
	 * @return index
	 */
	public byte getIndex() {
		return index;
	}

	/**
	 * Gets name of CustomInstrument
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets file name of the sound
	 * @return file name
	 */
	public String getSoundFileName() {
		return soundFileName;
	}
}
