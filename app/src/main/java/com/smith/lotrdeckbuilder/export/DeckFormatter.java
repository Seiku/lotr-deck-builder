package com.smith.lotrdeckbuilder.export;

import com.smith.lotrdeckbuilder.game.Deck;

public interface DeckFormatter {
    String fromDeck(Deck deck);
}
