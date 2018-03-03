package com.smith.lotrdeckbuilder.importer;

/**
 * Created by sebast on 26/06/16.
 */

public class DeckFormatNotSupportedException extends Exception {
    public DeckFormatNotSupportedException(String detailMessage) {
        super(detailMessage);
    }
}
