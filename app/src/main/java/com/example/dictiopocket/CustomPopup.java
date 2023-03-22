package com.example.dictiopocket;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class CustomPopup extends Dialog {

    private String title;
    private String subtitle;
    private Button yesButton, noButton;
    private TextView titleView, subTitleView;

    public CustomPopup(Activity activity) {
        super(activity);
        setContentView(R.layout.popup_template);

        this.title = "Titre";
        this.subtitle = "Sous-titre";
        this.yesButton = findViewById(R.id.yesButton);
        this.noButton = findViewById(R.id.noButton);
        this.titleView = findViewById(R.id.title);
        this.subTitleView = findViewById(R.id.subtitle);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public Button getYesButton() {
        return yesButton;
    }

    public Button getNoButton() {
        return noButton;
    }

    public void build() {
        show();
        titleView.setText(title);
        subTitleView.setText(subtitle);
    }
}
