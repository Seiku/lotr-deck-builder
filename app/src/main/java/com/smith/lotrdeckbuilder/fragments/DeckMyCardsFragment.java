package com.smith.lotrdeckbuilder.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.smith.lotrdeckbuilder.DeckActivity;
import com.smith.lotrdeckbuilder.R;
import com.smith.lotrdeckbuilder.ViewDeckFullscreenActivity;
import com.smith.lotrdeckbuilder.adapters.ExpandableDeckCardListAdapter;
import com.smith.lotrdeckbuilder.adapters.ExpandableDeckCardListAdapter.OnButtonClickListener;
import com.smith.lotrdeckbuilder.db.DatabaseHelper;
import com.smith.lotrdeckbuilder.game.Card;
import com.smith.lotrdeckbuilder.game.Deck;
import com.smith.lotrdeckbuilder.helper.AppManager;
import com.smith.lotrdeckbuilder.helper.Sorter;
import com.smith.lotrdeckbuilder.interfaces.OnDeckChangedListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class DeckMyCardsFragment extends Fragment implements OnDeckChangedListener {

    // Saved Instance
    private static final String SAVED_SELECTED_CARD_CODE = "CARD_CODE";

    private OnDeckChangedListener mListener;

    private Deck mDeck;

    private Card currentCard;
    private View mainView;

    // TODO: Change to ExpandableStickyListAdapter https://github.com/emilsjolander/StickyListHeaders
    private ExpandableListView lstDeckCards;
    private ExpandableDeckCardListAdapter mDeckCardsAdapter;

    // Database
    DatabaseHelper mDb;

    private ArrayList<String> mListHeaders;
    private HashMap<String, ArrayList<Card>> mListCards = new HashMap<String, ArrayList<Card>>();

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //
        setHasOptionsMenu(true);

        // Inflate
        mainView = inflater.inflate(R.layout.fragment_deck_cards, container, false);

        // Get the arguments
        mDeck = AppManager.getInstance().getDeck(getArguments().getLong(DeckActivity.ARGUMENT_DECK_ID));

        // The GUI items
        lstDeckCards = (ExpandableListView) mainView.findViewById(R.id.lstDeckCards);

        // Database
        mDb = new DatabaseHelper(getActivity());

        // Set the list view
        setListView();

        return mainView;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //
        switch (item.getItemId()) {

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnDeckChangedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnDeckChangedListener");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Remember the selected card
        if (currentCard != null)
            outState.putString(SAVED_SELECTED_CARD_CODE, currentCard.getCode());
    }

    private void refreshCardList() {
        // Generate a new card list to display and notify the adapter
        for (String theHeader : mListHeaders) {
            mListCards.put(theHeader, new ArrayList<Card>());
        }
        for (Card theCard : mDeck.getCards()) {
            // Only add the cards that are on my side
            // Do not add the identities
            if (!theCard.getTypeCode().equals(Card.Type.IDENTITY) && theCard.getSideCode().equals(mDeck.getIdentity().getSideCode())) {
                if (mListCards.get(theCard.getTypeCode()) == null)
                    mListCards.put(theCard.getTypeCode(), new ArrayList<Card>());
                mListCards.get(theCard.getTypeCode()).add(theCard);
            }
        }
        sortListCards();
        mDeckCardsAdapter.notifyDataSetChanged();
    }

    private void setListView() {
        // Get the cards
        mListHeaders = AppManager.getInstance().getAllCards().getCardType(mDeck.getIdentity().getSideCode());
        mListHeaders.remove(mDeck.getIdentity().getTypeCode()); // Remove the Identity category
        Collections.sort(mListHeaders);

        // Adapters
        mDeckCardsAdapter = new ExpandableDeckCardListAdapter(getActivity(), mListHeaders, mListCards, mDeck, true, new OnButtonClickListener() {

            @Override
            public void onPlusClick(Card card) {
                // Update the listview
                mListener.onDeckCardsChanged();
            }

            @Override
            public void onMinusClick(Card card) {
                // Remove zero cards
                if (mDeck.getCardCount(card) <= 0) {
                    mListCards.get(card.getTypeCode()).remove(card);
                }
                // Update the list
                mListener.onDeckCardsChanged();
            }
        });
        lstDeckCards.setAdapter(mDeckCardsAdapter);
        lstDeckCards.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                currentCard = (Card) mDeckCardsAdapter.getChild(groupPosition, childPosition);
                Intent intent = new Intent(getActivity(), ViewDeckFullscreenActivity.class);
                intent.putExtra(ViewDeckFullscreenActivity.EXTRA_CARD_CODE, currentCard.getCode());
                startActivity(intent);
                return false;
            }
        });

        // Refresh the cards list
        refreshCardList();
    }

    private void sortListCards() {
        // Sort by faction,
        // My cards must be sorted by type
        for (String strCat : mListHeaders) {
            //Collections.sort(mListCards.get(strCat), new Sorter.CardSorterByFaction());
            ArrayList<Card> arrCards = mListCards.get(strCat);
            if (arrCards != null) {
                Collections.sort(arrCards, new Sorter.CardSorterByFactionWithMineFirst(mDeck.getIdentity()));
            }
        }

    }

    @Override
    public void onDeckNameChanged(Deck deck, String name) {
    }

    @Override
    public void onDeckDeleted(Deck deck) {
    }

    @Override
    public void onDeckCloned(Deck deck) {
    }

    @Override
    public void onDeckCardsChanged() {
        if (!isAdded()) return;
        // Refresh my cards
        refreshCardList();
    }

    @Override
    public void onDeckIdentityChanged(Card newIdentity) {
        if (!isAdded()) return;
        setListView();
    }

    @Override
    public void onSettingsChanged() {

    }

}
