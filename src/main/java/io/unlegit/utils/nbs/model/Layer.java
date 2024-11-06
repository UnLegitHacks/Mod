package io.unlegit.utils.nbs.model;

import java.util.HashMap;

/**
 * Represents a series of notes in Note Block Studio.
 * A layer can have a maximum of 1 note per tick.
 */
public class Layer
{
    private final HashMap<Integer, Note> notesAtTicks = new HashMap<>();
    public byte volume = 100;
    public int panning = 100;
    public String name = "";

    /**
     * Gets the note played at a given tick
     */
    public Note getNote(int tick)
    {
        return notesAtTicks.get(tick);
    }

    /**
     * Sets the given note at the given tick in the Layer
     */
    public void setNote(int tick, Note note)
    {
        notesAtTicks.put(tick, note);
    }
}
