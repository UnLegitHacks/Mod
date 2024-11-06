package io.unlegit.utils.nbs.model;

import java.io.File;
import java.util.HashMap;

/**
 * Represents a Note Block Studio project
 */
public record Song(float speed, HashMap<Integer, Layer> layerHashMap, short songHeight, short length, String title,
                   String author, String originalAuthor, String description, File path, int firstCustomInstrumentIndex,
                   CustomInstrument[] customInstruments, boolean stereo) {}
