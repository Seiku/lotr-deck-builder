package com.smith.lotrdeckbuilder.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smith.lotrdeckbuilder.DeckActivity;
import com.smith.lotrdeckbuilder.R;
import com.smith.lotrdeckbuilder.db.DatabaseHelper;
import com.smith.lotrdeckbuilder.game.Card;
import com.smith.lotrdeckbuilder.game.Deck;
import com.smith.lotrdeckbuilder.helper.AppManager;
import com.smith.lotrdeckbuilder.helper.ImageDisplayer;
import com.smith.lotrdeckbuilder.interfaces.OnDeckChangedListener;

public class DeckInfoFragment extends Fragment implements OnDeckChangedListener {

    // Listener
    OnDeckChangedListener mListener;

    View mainView;
    TextView txtDeckName;
    TextView txtDeckDescription;
    ImageView imgIdentity;
    Deck mDeck;

    // Database
    DatabaseHelper mDb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Main View
        mainView = (View) inflater.inflate(R.layout.fragment_deck_info, container, false);

        // Arguments
        Bundle bundle = getArguments();
        mDeck = AppManager.getInstance().getDeck(bundle.getLong(DeckActivity.ARGUMENT_DECK_ID));

        // GUI
        imgIdentity = (ImageView) mainView.findViewById(R.id.imgIdentity);
        txtDeckName = (TextView) mainView.findViewById(R.id.lblLabel);
        txtDeckDescription = (TextView) mainView.findViewById(R.id.txtDeckDescription);

        // Variables
        mDb = new DatabaseHelper(getActivity());

        // Set the info
        ImageDisplayer.fill(imgIdentity, mDeck.getIdentity(), getActivity());
        txtDeckName.setText(mDeck.getName());
        txtDeckDescription.setText(mDeck.getNotes());

        // Events
        txtDeckName.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                //

            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                mDeck.setName(arg0.toString());
                mListener.onDeckNameChanged(mDeck, arg0.toString());
	            ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(arg0.toString());
            }

        });
        txtDeckDescription.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mDeck.setNotes(s.toString());
                //mDb.updateDeck(mDeck);
            }
        });

        return mainView;

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
    public void onDeckNameChanged(Deck deck, String name) {
        //

    }

    @Override
    public void onDeckDeleted(Deck deck) {
        //

    }

    @Override
    public void onDeckCloned(Deck deck) {
        //

    }

    @Override
    public void onDeckCardsChanged() {
        //

    }

    @Override
    public void onDeckIdentityChanged(Card newIdentity) {
        if (!isAdded()) return;
        // Update the image
        if (getActivity() != null)
            imgIdentity.setImageBitmap(mDeck.getIdentity().getImage(getActivity()));
    }

    @Override
    public void onSettingsChanged() {

    }

}
