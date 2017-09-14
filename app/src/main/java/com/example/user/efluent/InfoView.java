package com.example.user.efluent;

import android.content.Context;
import android.icu.text.IDNA;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.expand.ChildPosition;
import com.mindorks.placeholderview.annotations.expand.ParentPosition;

/**
 * Created by imanerai on 4/9/17.
 */

@Layout(R.layout.feed_item)
public class InfoView {

    @ParentPosition
    private int mParentPosition;

    @ChildPosition
    private int mChildPosition;

    @View(R.id.titleTxt)
    private TextView titleTxt;

    @View(R.id.timeTxt)
    private TextView timeTxt;

    private Message message;
    private Context mContext;

    public InfoView(Context context, Message mess) {
        mContext = context;
        message = mess;
    }

    @Resolve
    private void onResolved() {
        titleTxt.setText(message.getText());
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String s = df.format("EEE dd MMM yyyy : kk:mm",message.getTime()).toString();
        timeTxt.setText(s);
    }
}
