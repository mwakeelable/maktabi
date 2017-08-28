package com.linked_sys.maktabi.activities;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.linked_sys.maktabi.R;
import com.linked_sys.maktabi.adapters.CircleTransform;
import com.linked_sys.maktabi.core.CacheHelper;
import com.linked_sys.maktabi.network.ApiEndPoints;
import com.linked_sys.maktabi.utils.SelectableRoundedImageView;

public class ProfileActivity extends BaseActivity {
    TextView nameTXT, nationalIDTXT, cardNoTXT;
    TextView uberName, uberMobileTXT;
    TextView careemName, careemMobileTXT, careemIDTXT;
    LinearLayout careemDataContainer, uberDataContainer;
    SelectableRoundedImageView profileIMG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nameTXT = (TextView) findViewById(R.id.fullNameTxt);
        uberMobileTXT = (TextView) findViewById(R.id.uberMobileTxt);
        careemMobileTXT = (TextView) findViewById(R.id.careemMobileTxt);
        nationalIDTXT = (TextView) findViewById(R.id.nIDTxt);
        cardNoTXT = (TextView) findViewById(R.id.cardNoTxt);
        profileIMG = (SelectableRoundedImageView) findViewById(R.id.profile_image);
        uberName = (TextView) findViewById(R.id.uberNameTxt);
        careemName = (TextView) findViewById(R.id.careemNameTxt);
        careemDataContainer = (LinearLayout) findViewById(R.id.careemDataContainer);
        uberDataContainer = (LinearLayout) findViewById(R.id.uberDataContainer);
        careemIDTXT = (TextView) findViewById(R.id.careemIDTxt);
        setData();
    }

    private void setData() {
        nameTXT.setText(CacheHelper.getInstance().captainData.get(session.KEY_NAME));
        uberMobileTXT.setText(CacheHelper.getInstance().captainData.get(session.KEY_UBER_MOBILE));
        careemMobileTXT.setText(CacheHelper.getInstance().captainData.get(session.KEY_CAREEM_MOBILE));
        nationalIDTXT.setText(CacheHelper.getInstance().captainData.get(session.KEY_NID));
        cardNoTXT.setText(CacheHelper.getInstance().captainData.get(session.KEY_CARD_NO));
        uberName.setText(CacheHelper.getInstance().captainData.get(session.KEY_UBER_NAME));
        careemName.setText(CacheHelper.getInstance().captainData.get(session.KEY_CAREEM_NAME));
        careemIDTXT.setText(CacheHelper.getInstance().captainData.get(session.KEY_CAREEM_ID));
        if (!CacheHelper.getInstance().captainData.get(session.KEY_IMAGE).equals("null")) {
            Glide.with(this)
                    .load(ApiEndPoints.BASE_URL + CacheHelper.getInstance().captainData.get(session.KEY_IMAGE))
                    .asBitmap()
                    .transform(new CircleTransform(this))
                    .into(new SimpleTarget<Bitmap>(300, 300) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                            profileIMG.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                            profileIMG.setImageDrawable(getResources().getDrawable(R.drawable.avatar_placeholder));
                        }
                    });
        }

        if (CacheHelper.getInstance().captainData.get("isCareem").equals("true")) {
            careemDataContainer.setVisibility(View.VISIBLE);
        } else {
            careemDataContainer.setVisibility(View.GONE);
        }

        if (CacheHelper.getInstance().captainData.get("isUber").equals("true")) {
            uberDataContainer.setVisibility(View.VISIBLE);
        } else {
            uberDataContainer.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_profile;
    }

}
