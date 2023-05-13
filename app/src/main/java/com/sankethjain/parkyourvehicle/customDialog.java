package com.sankethjain.parkyourvehicle;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class customDialog extends Dialog implements
        android.view.View.OnClickListener {

        private Activity c;
        private Dialog d;
        private Button ok;
    private AdView mAdView;

  public customDialog(Activity a) {
            super(a);
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.customview);
            mAdView = findViewById(R.id.adView2);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

            ok = findViewById(R.id.buttonOk);
            ok.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonOk:
                    this.dismiss();
                    break;
                default:
                    break;
            }
            dismiss();
        }
}
