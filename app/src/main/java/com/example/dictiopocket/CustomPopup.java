package com.example.dictiopocket;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

/*
Cette classe permet de créer un popup et de modifier le text a afficher
 */
public class CustomPopup extends Dialog {

    private String title;
    private String subtitle;
    private Button yesButton, noButton;
    private TextView titleView, subTitleView;

    /*
    Le constructeur qui crée une nouvelle instance de la classe et définit
    la vue à utiliser (popup_template.xml).
    Il initialise également les valeurs par défaut pour les titres et les sous-titres,
    et récupère les références pour les boutons et les TextView
     */
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

    /*
    Cette méthode permet de modifier le titre de la popup
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /*
    Cette méthode permet de modifier le sous-titrte de la popup
     */
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    /*
    Cette méthode renvoie la référence du boutons Oui qui permet de définir
    une action à effectuer lorsque l'utilisateur clique dessus
     */
    public Button getYesButton() {
        return yesButton;
    }

    /*
    Cette méthode renvoie la référence du boutons Non qui permet de définir
    une action à effectuer lorsque l'utilisateur clique dessus
     */
    public Button getNoButton() {
        return noButton;
    }

    /*
    Cette méthode permet de construire et afficher la popup.
    Elle définit les textes pour le titres et le sous-titre
    puis affiche la boîte de dialogue
     */
    public void build() {
        show();
        titleView.setText(title);
        subTitleView.setText(subtitle);
    }
}
