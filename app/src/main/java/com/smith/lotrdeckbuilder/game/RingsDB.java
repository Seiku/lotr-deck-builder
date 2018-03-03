package com.smith.lotrdeckbuilder.game;

import com.smith.lotrdeckbuilder.SettingsActivity;
import com.smith.lotrdeckbuilder.helper.AppManager;

public class RingsDB {

    public static final String BASE_URL = "https://ringsdb.com";
    public static final String URL_API_SEARCH = "/api/search/";
    private static final String URL_GET_ALL_CARDS = "https://ringsdb.com/api/public/cards?_locale=%s";
    private static final String URL_GET_ALL_PACKS = "https://ringsdb.com/api/public/packs";
    private static final String URL_GET_MWL = "https://ringsdb.com/api/public/mwl";

    public static String getAllCardsUrl() {
        return String.format(URL_GET_ALL_CARDS, AppManager.getInstance().getSharedPrefs().getString(SettingsActivity.KEY_PREF_LANGUAGE, "en"));
    }

    public static String getAllPacksUrl() {
        return URL_GET_ALL_PACKS;
    }

    public static String getMWLUrl() { return URL_GET_MWL; }
}
