package io.unlegit.utils.nbs;

import io.unlegit.utils.nbs.model.CustomInstrument;
import io.unlegit.utils.nbs.model.Layer;
import io.unlegit.utils.nbs.model.Note;
import io.unlegit.utils.nbs.model.Song;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Utils for reading Note Block Studio data
 */
public class NBSDecoder
{
    /**
     * Parses a song from a Note Block Studio project file (.nbs)
     * @param songFile .nbs file
     * @return A song object representing a Note Block Studio project
     * @see Song
     */
    public static Song parse(File songFile)
    {
        try
        {
            return parse(new FileInputStream(songFile), songFile);
        }

        catch (FileNotFoundException e)
        {
            return null;
        }
    }

    /**
     * Parses a song from an InputStream and a Note Block Studio project file (.nbs)
     * @param inputStream of a .nbs file
     * @param songFile representing a .nbs file
     * @return A song object representing the given .nbs file
     * @see Song
     */
    private static Song parse(InputStream inputStream, File songFile)
    {
        HashMap<Integer, Layer> layerHashMap = new HashMap<>();
        boolean stereo = false;

        try
        {
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            short length = readShort(dataInputStream);
            int fci = 10; // Backward compatibility - most songs with old structures are from 1.12
            int nbsVersion = 0;

            if (length == 0)
            {
                nbsVersion = dataInputStream.readByte();
                fci = dataInputStream.readByte();

                if (nbsVersion >= 3)
                    length = readShort(dataInputStream);
            }

            short songHeight = readShort(dataInputStream);
            String title = readString(dataInputStream), author = readString(dataInputStream),
                    originalAuthor = readString(dataInputStream), description = readString(dataInputStream);

            float speed = readShort(dataInputStream) / 100F;
            dataInputStream.readBoolean(); // Auto-save
            dataInputStream.readByte(); // Auto-save duration
            dataInputStream.readByte(); // x/4ths, Time signature
            readInt(dataInputStream); // Minutes spent on project
            readInt(dataInputStream); // Left clicks
            readInt(dataInputStream); // Right clicks
            readInt(dataInputStream); // Blocks added
            readInt(dataInputStream); // Blocks removed
            readString(dataInputStream); // .mid/.schematic file name

            if (nbsVersion >= 4)
            {
                dataInputStream.readByte(); // Loop toggle
                dataInputStream.readByte(); // Max loop count
                readShort(dataInputStream); // Loop start tick
            }

            short tick = -1, jumpTicks;

            while ((jumpTicks = readShort(dataInputStream)) != 0)
            {
                tick += jumpTicks;
                short layer = -1, jumpLayers; // Jumps till the next layer

                while ((jumpLayers = readShort(dataInputStream)) != 0)
                {
                    layer += jumpLayers;
                    byte instrument = dataInputStream.readByte();
                    byte key = dataInputStream.readByte();
                    byte velocity = 100;
                    int panning = 100;
                    short pitch = 0;

                    if (nbsVersion >= 4)
                    {
                        velocity = dataInputStream.readByte(); // Note Block velocity
                        panning = 200 - dataInputStream.readUnsignedByte(); // Note panning, 0 is right in nbs format
                        pitch = readShort(dataInputStream); // Note Block pitch
                    }

                    if (panning != 100)
                        stereo = true;

                    setNote(layer, tick, new Note(instrument, key, velocity, panning, pitch), layerHashMap);
                }
            }

            if (nbsVersion > 0 && nbsVersion < 3)
                length = tick;

            for (int i = 0; i < songHeight; i++)
            {
                Layer layer = layerHashMap.get(i);
                String name = readString(dataInputStream);

                if (nbsVersion >= 4)
                    dataInputStream.readByte(); // Layer lock

                byte volume = dataInputStream.readByte();
                int panning = 100;

                if (nbsVersion >= 2)
                    panning = 200 - dataInputStream.readUnsignedByte(); // Layer stereo, 0 is right in NBS format

                if (panning != 100)
                    stereo = true;

                if (layer != null)
                {
                    layer.name = name;
                    layer.volume = volume;
                    layer.panning = panning;
                }
            }

            // The count of custom instruments
            byte customAmnt = dataInputStream.readByte();
            CustomInstrument[] customInstrumentsArray = new CustomInstrument[customAmnt];

            for (int index = 0; index < customAmnt; index++)
            {
                customInstrumentsArray[index] = new CustomInstrument((byte) index, readString(dataInputStream), readString(dataInputStream));
                dataInputStream.readByte(); // Pitch
                dataInputStream.readByte(); // Key
            }

            ArrayList<CustomInstrument> customInstruments = CompatibilityUtils.getVersionCustomInstrumentsForSong(fci);
            customInstruments.addAll(Arrays.asList(customInstrumentsArray));
            customInstrumentsArray = customInstruments.toArray(customInstrumentsArray);
            return new Song(speed, layerHashMap, songHeight, length, title, author, originalAuthor, description, songFile, fci, customInstrumentsArray, stereo);
        }

        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Sets a note at a tick in a song
     */
    private static void setNote(int layerIndex, int ticks, Note note, HashMap<Integer, Layer> layerHashMap)
    {
        Layer layer = layerHashMap.get(layerIndex);

        if (layer == null)
        {
            layer = new Layer();
            layerHashMap.put(layerIndex, layer);
        }

        layer.setNote(ticks, note);
    }

    private static short readShort(DataInputStream dataInputStream) throws IOException
    {
        int a = dataInputStream.readUnsignedByte(), b = dataInputStream.readUnsignedByte();
        return (short) (a + (b << 8));
    }

    private static int readInt(DataInputStream dataInputStream) throws IOException
    {
        int a = dataInputStream.readUnsignedByte(), b = dataInputStream.readUnsignedByte(),
            c = dataInputStream.readUnsignedByte(), d = dataInputStream.readUnsignedByte();
        return (a + (b << 8) + (c << 16) + (d << 24));
    }

    private static String readString(DataInputStream dataInputStream) throws IOException
    {
        int length = readInt(dataInputStream);
        StringBuilder builder = new StringBuilder(length);

        for (; length > 0; --length)
        {
            char c = (char) dataInputStream.readByte();
            if (c == 0x0D) c = ' ';
            builder.append(c);
        }

        return builder.toString();
    }
}
