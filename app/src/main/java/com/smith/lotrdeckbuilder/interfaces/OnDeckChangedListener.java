package com.smith.lotrdeckbuilder.interfaces;

import com.smith.lotrdeckbuilder.game.Card;
import com.smith.lotrdeckbuilder.game.Deck;

public interface OnDeckChangedListener {
    void onDeckNameChanged(Deck deck, String name);

    void onDeckDeleted(Deck deck);

    void onDeckCloned(Deck deck);

    void onDeckCardsChanged();

    void onDeckIdentityChanged(Card newIdentity);

    void onSettingsChanged();
}
