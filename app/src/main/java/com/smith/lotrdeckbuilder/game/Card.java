package com.smith.lotrdeckbuilder.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.smith.lotrdeckbuilder.R;
import com.smith.lotrdeckbuilder.SettingsActivity;
import com.smith.lotrdeckbuilder.helper.AppManager;

import org.json.JSONObject;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class Card {

    public static final String NAME_CODE = "code"; // eg 01001
    public static final String NAME_COST = "cost";
    public static final String NAME_TITLE = "title";
    public static final String NAME_TYPE_CODE = "type_code";
    public static final String NAME_SUBTYPE = "traits";
    public static final String NAME_TEXT = "text";
    public static final String NAME_SPHERE_CODE = "sphere_code";
    public static final String NAME_FLAVOR = "flavor";
    public static final String NAME_ILLUSTRATOR = "illustrator";
    public static final String NAME_NUMBER = "position";
    public static final String NAME_QUANTITY = "quantity";
    public static final String NAME_SET_CODE = "pack_code";
    public static final String NAME_UNIQUENESS = "is_unique";
    public static final String NAME_DECK_LIMIT = "deck_limit";
    public static final String NAME_THREAT = "threat";
    public static final String NAME_WILLPOWER = "willpower";
    public static final String NAME_ATTACK = "attack";
    public static final String NAME_DEFENSE = "defense";
    public static final String NAME_HEALTH = "health";
    public static final String NAME_IMAGE_URL_OVERRIDE = "image_url";
    public static final String NAME_IMAGE_SRC = "imagesrc";

    private String code;
    private String cost;
    private String title;
    private String typeCode;
    private String subtype;
    private String text;
    private String sphereCode;
    private String flavor;
    private String illustrator;
    private String number;
    private String quantity;
    private String setCode;
    private String deckLimit;
    private int threat;
    private int willpower;
    private int attack;
    private int defense;
    private int health;
    private boolean uniqueness;
    private URL imagesrc;

    public Card(JSONObject json) {

        try {
            this.code = json.optString(NAME_CODE);
            this.cost = json.optString(NAME_COST);
            this.title = json.optString(NAME_TITLE);
            this.typeCode = json.optString(NAME_TYPE_CODE);
            this.subtype = json.optString(NAME_SUBTYPE);
            this.text = json.optString(NAME_TEXT);
            this.deckLimit = json.optString(NAME_DECK_LIMIT);
            this.sphereCode = json.optString(NAME_SPHERE_CODE);
            this.threat = json.optInt(NAME_THREAT, 0);
            this.flavor = json.optString(NAME_FLAVOR);
            this.illustrator = json.optString(NAME_ILLUSTRATOR);
            this.number = json.optString(NAME_NUMBER);
            this.quantity = json.optString(NAME_QUANTITY);
            this.setCode = json.optString(NAME_SET_CODE);
            this.uniqueness = json.optBoolean(NAME_UNIQUENESS);
            String imageUrl = json.optString(NAME_IMAGE_URL_OVERRIDE);
            if(imageUrl == null || imageUrl.length() < 1) {
                imageUrl = json.optString(NAME_IMAGE_SRC);
            }
            this.imagesrc = new URL(imageUrl);
            this.willpower = json.optInt(NAME_WILLPOWER, 0);
            this.attack = json.optInt(NAME_ATTACK, 0);
            this.defense = json.optInt(NAME_DEFENSE, 0);
            this.health = json.optInt(NAME_HEALTH, 0);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    //TODO continue with getters and other methods

    public String getCode() {
        return code;
    }

    public String getCost() {
        return cost;
    }

    public String getTitle() {
        return title;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public String getSubtype() {
        return subtype;
    }

    public String getText() {
        return text;
    }

    public String getSphereCode() {
        return sphereCode;
    }

    public String getFlavor() {
        return flavor;
    }

    public String getIllustrator() {
        return illustrator;
    }

    public int getInfluenceLimit() {
        try {
            return Integer.parseInt(influenceLimit);
        } catch (Exception e) {
            return Integer.MAX_VALUE;
        }
    }

    public int getMinimumDeckSize() {
        if (minimumDeckSize.equals(""))
            return 0;
        else
            return Integer.parseInt(minimumDeckSize);
    }

    public int getNumber() {
        if (number.equals(""))
            return 0;
        else
            return Integer.parseInt(number);
    }

    public int getQuantity() {
        if (quantity.equals(""))
            return 0;
        else
            return Integer.parseInt(quantity);
    }

    public String getSetName() {
        return AppManager.getInstance().getPackByCode(setCode).getName();
    }

    public String getSetCode() { return setCode; }

    public String getSideCode() {
        return sideCode;
    }

    public boolean isUniqueness() {
        return uniqueness;
    }

    public int getAgendaPoints() {
        return agendaPoints;
    }

    public int getAdvancementCost() {
        return advancementCost;
    }

    public int getMemoryUnits() {
        return memoryunits;
    }

    public int getTrashCost() {
        return trash;
    }

    public int getStrength() {
        return strength;
    }

    public URL getImagesrc() {
        return imagesrc;
    }


    /**
     * Splits subtype string by " - " into array of subtypes.
     * @return Array of subtype strings.
     * If subtype string is empty, array will contain a single empty string.
     */
    public String[] getSubtypeArray() {
        return subtype.split(" - ");
    }

    /**
     * Calculates how many of that card you can add in a deck
     * - Checks how many core decks you have and calculate
     *
     * @return How many cards you can use in a deck
     */
    public int getMaxCardCount() {
        try {
            if (this.setCode.equals(SetName.CORE_SET) || this.setCode.equals(SetName.REVISED_CORE_SET)) {
                int iAmountCoreDecks = Integer.parseInt(AppManager.getInstance().getSharedPrefs().getString(SettingsActivity.KEY_PREF_AMOUNT_OF_CORE_DECKS, "3"));
                return Math.min(iAmountCoreDecks * Integer.parseInt(this.quantity), Deck.MAX_INDIVIDUAL_CARD);
            } else {
                return Integer.parseInt(this.quantity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Deck.MAX_INDIVIDUAL_CARD;
        }
    }

    /**
     * @return Formatted text with images
     */
    public SpannableString getFormattedText(Context context) {
        return getFormattedString(context, this.getText());
    }

    public static SpannableString getFormattedString(Context context, String text) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("[agenda]", R.drawable.agenda);
        map.put("[click]", R.drawable.click);
        map.put("[trash]", R.drawable.trash);
        map.put("[credit]", R.drawable.credits);
        map.put("[subroutine]", R.drawable.subroutine);
        map.put("[mu]", R.drawable.memory_unit);
        map.put("[recurring-credit]", R.drawable.credit_recurr);
        map.put("[link]", R.drawable.links);
        map.put("[fist]", R.drawable.fist);

        // replace all occurences
        SpannableString span = new SpannableString(Html.fromHtml(text.replace("\n", "<br />")));
        for (String txt : map.keySet()) {
            int index = span.toString().toLowerCase().indexOf(txt);
            while (index >= 0) {
                span.setSpan(new ImageSpan(context, map.get(txt), ImageSpan.ALIGN_BOTTOM), index, index + txt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                index = span.toString().indexOf(txt, index + 1);
            }
        }

        return span;
    }

    public Bitmap getImage(Context context) {
        return BitmapFactory.decodeFile(new File(context.getCacheDir(), this.getImageFileName()).getAbsolutePath());
    }

    public Bitmap getSmallImage(Context context) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        return BitmapFactory.decodeFile(new File(context.getCacheDir(), this.getImageFileName()).getAbsolutePath(), options);
    }

    public boolean isImageFileExists(Context context) {
        if (context == null) return false;
        //File f = new File(context.getFilesDir(), this.getImageFileName());
        File f = new File(context.getCacheDir(), this.getImageFileName());
        return f.exists();
    }

    @Override
    public String toString() {
        //
        return this.getTitle();
    }

    public String getImageFileName() {
        return this.getCode() + AppManager.EXT_CARDS_IMAGES;
    }

    public String getFactionImageResName() {
        String lowCaseFaction = this.getSphereCode().toLowerCase();
        if (lowCaseFaction.startsWith(Faction.FACTION_NEUTRAL)) {
            lowCaseFaction = this.getSideCode() + "_" + lowCaseFaction;
        }
        lowCaseFaction = lowCaseFaction.replace("-", "_");
        lowCaseFaction = lowCaseFaction.replace(" ", "_");
        return lowCaseFaction;
    }

    public int getFactionImageRes(Context context) {
        if (getSphereCode().startsWith(Faction.FACTION_NEUTRAL)) return R.drawable.neutral;
        return context.getResources().getIdentifier(getFactionImageResName(), "drawable", context.getPackageName());
    }

    public int getMWLInfluence() {
        return this.mostWantedInfluence;
    }

    public boolean isMostWanted() {
        return this.mostWantedInfluence > 0;
    }

    /**
     * Gets ice/icebreaker main subtype.
     * @return The main subtype of an ice or icebreaker (if detected) card, otherwise an empty string.
     */
    public String getIceOrIcebreakerSubtype() {
        switch (typeCode)
        {
            case Type.ICE:
                return getSubtypeArray()[0];
            case Type.PROGRAM:
                String[] subtypes = getSubtypeArray();
                // FIXME: Can't detect icebreakers on non-english cards.
                return (subtypes.length > 1 && subtypes[0].equals("Icebreaker")) ? subtypes[1] : "";
            default:
                return "";
        }
    }

    @Override
    public boolean equals(Object o) {
        return ((Card) o).getCode().equals(this.getCode());
    }

    public static class Faction {
        public static final String FACTION_NEUTRAL = "neutral-";
        public static final String FACTION_SHAPER = "shaper";
        public static final String FACTION_CRIMINAL = "criminal";
        public static final String FACTION_WEYLAND_CONSORTIUM = "weyland-consortium";
        public static final String FACTION_ANARCH = "anarch";
        public static final String FACTION_HAAS_BIOROID = "haas-bioroid";
        public static final String FACTION_JINTEKI = "jinteki";
        public static final String FACTION_NBN = "nbn";
    }

    public static class Side {
        public static final String SIDE_RUNNER = "runner";
        public static final String SIDE_CORPORATION = "corp";
    }

    public static class Type {
        public static final String AGENDA = "agenda";
        public static final String ASSET = "asset";
        public static final String EVENT = "event";
        public static final String ICE = "ice";
        public static final String IDENTITY = "identity";
        public static final String HARDWARE = "hardware";
        public static final String OPERATION = "operation";
        public static final String PROGRAM = "program";
        public static final String RESOURCE = "resource";
        public static final String UPGRADE = "upgrade";
    }

    public static class SubTypeCode {
        public static final String ALLIANCE = "alliance";
    }

    public static class SetName {
//        public static final String ALTERNATES = "Alternates";
        public static final String CORE_SET = "core";
        public static final String REVISED_CORE_SET = "core2";
    }

    public static class SpecialCards {
        public static final String CARD_THE_PROCESSOR = "03029";
        public static final String CARD_ANDROMEDA = "02083";
        public static final String CARD_CUSTOM_BIOTICS_ENGINEERED_FOR_SUCCESS = "03002";
        public static final String APEX = "09029";
    }


}
