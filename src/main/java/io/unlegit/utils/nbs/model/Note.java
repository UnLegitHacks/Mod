package io.unlegit.utils.nbs.model;

public record Note(byte instrument, byte key, byte velocity, int panning, short pitch) {}
