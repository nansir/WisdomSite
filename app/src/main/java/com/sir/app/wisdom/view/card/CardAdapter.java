package com.sir.app.wisdom.view.card;

import androidx.cardview.widget.CardView;

/**
 * Created by zhuyinan on 2020/4/12.
 */
public interface CardAdapter {

    int MAX_ELEVATION_FACTOR = 2;

    float getBaseElevation();

    CardView getCardViewAt(int position);

    int getCount();
}
