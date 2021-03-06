package com.smith.lotrdeckbuilder.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smith.lotrdeckbuilder.R;
import com.smith.lotrdeckbuilder.game.Card;
import com.smith.lotrdeckbuilder.helper.ImageDisplayer;

import java.util.ArrayList;

public class HandCardsListAdapter extends ArrayAdapter<Card> {

    private static class ViewHolderItem {
        ImageView imgImage;
        TextView lblTitle;
        TextView lblText;
    }

    private LayoutInflater mInflater;
    private Context mContext;

    public HandCardsListAdapter(Context context, ArrayList<Card> cards) {
        super(context, 0, cards);
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolderItem viewHolder;
        if (convertView == null) {
            // Inflate the layout
            convertView = mInflater.inflate(R.layout.list_view_item_hand_card, parent, false);

            // Set up the ViewHolder
            viewHolder = new ViewHolderItem();
            viewHolder.imgImage = (ImageView) convertView.findViewById(R.id.imgImage);
            viewHolder.lblTitle = (TextView) convertView.findViewById(R.id.lblTitre);
            viewHolder.lblText = (TextView) convertView.findViewById(R.id.lblText);

            // Store the ViewHolder
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        // Get the object
        final Card card = this.getItem(position);

        // Assign the values
        if (card != null) {
            // Title
            String strUnique = (card.isUniqueness() ? mContext.getString(R.string.influence_char) + " " : "");
            if (!card.getSubtype().isEmpty()) {
                viewHolder.lblTitle.setText(strUnique + card.getTitle() + " (" + card.getSubtype() + ")");
            } else {
                viewHolder.lblTitle.setText(strUnique + card.getTitle());
            }
            viewHolder.lblText.setText(card.getFormattedText(mContext));
            ImageDisplayer.fillSmall(viewHolder.imgImage, card, mContext);
        }

        // Return the view
        return convertView;

    }

}
