package com.smith.lotrdeckbuilder.export;

import com.smith.lotrdeckbuilder.game.Card;
import com.smith.lotrdeckbuilder.game.Deck;

public class JintekiNet implements DeckFormatter {

    @Override
    public String fromDeck(Deck deck) {
        StringBuilder sb = new StringBuilder();
        for (Card card : deck.getCards()) {
            sb.append(String.format("%s %s\n", deck.getCardCount(card), card.getTitle()));
        }

        return sb.toString();
    }
}
