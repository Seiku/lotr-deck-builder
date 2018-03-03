package com.smith.lotrdeckbuilder.importer;

import com.smith.lotrdeckbuilder.game.Deck;

import java.util.ArrayList;

public interface IDeckImporter {
    ArrayList<Deck> toDecks();
}