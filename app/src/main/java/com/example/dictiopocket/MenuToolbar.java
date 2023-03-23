package com.example.dictiopocket;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MenuToolbar extends Dialog {


    public Button getBtnMenu1() {
        return btnMenu1;
    }

    public void setBtnMenu1(Button btnMenu1) {
        this.btnMenu1 = btnMenu1;
    }

    public Button getBtnMenu2() {
        return btnMenu2;
    }

    public void setBtnMenu2(Button btnMenu2) {
        this.btnMenu2 = btnMenu2;
    }

    private Button btnMenu1, btnMenu2;

    public MenuToolbar(Activity activity) {
        super(activity);
        setContentView(R.layout.menu_toolbar);
        Button btnMenu1 = findViewById(R.id.btnmenu1);
        Button btnMenu2 = findViewById(R.id.btnmenu2);
    }



}
